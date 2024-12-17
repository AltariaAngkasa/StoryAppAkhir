package com.example.mystoryappsubmission.ui.add


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mystoryappsubmission.R
import com.example.mystoryappsubmission.databinding.FragmentUploadBinding
import com.example.mystoryappsubmission.pref.compressImage
import com.example.mystoryappsubmission.pref.copyUriToFile
import com.example.mystoryappsubmission.ui.ViewModelFactory
import com.example.mystoryappsubmission.ui.camera.CameraActivity
import com.example.mystoryappsubmission.ui.story.StoryFragment
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryFragment : Fragment() {

    private lateinit var binding: FragmentUploadBinding
    private var currentImageUri: Uri? = null

    private val uploadViewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        uploadViewModel.apply {
            isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }

            statusMessage.observe(viewLifecycleOwner) { message ->
                if (!message.isNullOrBlank()) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    uploadViewModel.clearStatusMessage()
                }
            }

            isSuccess.observe(viewLifecycleOwner) { isSuccess ->
                if (isSuccess) {
                    navigateToStoryList()
                }
            }
        }
    }

    private fun setupListeners() {
        with(binding) {
            btGallery.setOnClickListener { startGallery() }
            btCamera.setOnClickListener { startCameraX() }
            btUpload.setOnClickListener { uploadImage() }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            currentImageUri = it
            showImage()
        } ?: Toast.makeText(context, "No Image Found", Toast.LENGTH_SHORT).show()
    }

    private fun showImage() {
        currentImageUri?.let { uri ->
            binding.previewImageView.setImageURI(uri)
        }
    }

    private fun startCameraX() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == CameraActivity.CAMERAX_RESULT) {
            currentImageUri = result.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun uploadImage() {
        val description = binding.etUploadDescription.text.toString()

        when {
            currentImageUri == null -> {
                showToast("Add photo")
            }

            description.isBlank() -> {
                showToast("Add description")
            }

            else -> {
                currentImageUri?.let { uri ->
                    val imageFile = copyUriToFile(uri, requireContext()).compressImage()
                    val requestBody = description.toRequestBody(getString(R.string.text_plain).toMediaType())
                    val requestImageFile = imageFile.asRequestBody(getString(R.string.image_jpeg).toMediaType())
                    val multipartBody = MultipartBody.Part.createFormData(
                        "photo",
                        imageFile.name,
                        requestImageFile
                    )

                    uploadViewModel.getUser().observe(viewLifecycleOwner) { user ->
                        user.token.let { token ->
                            uploadViewModel.uploadStory(token, multipartBody, requestBody)
                        }
                    }
                }
            }
        }
    }

    private fun navigateToStoryList() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_activity, StoryFragment())
            .commit()
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}








