package com.example.mystoryappsubmission.pref


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val FILENAME_PATTERN = "yyyyMMdd_HHmmss"
private const val MAX_FILE_SIZE = 1_000_000

fun generateTimeStamp(): String {
    return SimpleDateFormat(FILENAME_PATTERN, Locale.US).format(Date())
}

fun createTempFile(context: Context): File {
    val cacheDir = context.externalCacheDir ?: context.cacheDir
    return File.createTempFile(generateTimeStamp(), ".jpg", cacheDir)
}

fun copyUriToFile(uri: Uri, context: Context): File {
    val tempFile = createTempFile(context)
    context.contentResolver.openInputStream(uri)?.use { input ->
        FileOutputStream(tempFile).use { output ->
            input.copyTo(output)
        }
    }
    return tempFile
}

fun File.compressImage(): File {
    val bitmap = BitmapFactory.decodeFile(this.path).adjustOrientation(this)
    var quality = 100
    do {
        val byteArrayStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayStream)
        if (byteArrayStream.size() <= MAX_FILE_SIZE) break
        quality -= 5
    } while (quality > 0)
    FileOutputStream(this).use { fos ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos)
    }
    return this
}

fun Bitmap.adjustOrientation(file: File): Bitmap {
    val orientation = ExifInterface(file).getAttributeInt(
        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
    )
    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(this, 90F)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(this, 180F)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(this, 270F)
        else -> this
    }
}

fun rotateBitmap(bitmap: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(angle) }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}
