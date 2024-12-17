package com.example.mystoryappsubmission.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import androidx.core.util.Pair
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mystoryappsubmission.databinding.ItemMystoryBinding
import com.example.mystoryappsubmission.ui.detail.DetailStoryActivity
import com.example.storyappsubmission.data.response.ListStoryItem

class ListStoryAdapter : PagingDataAdapter<ListStoryItem, ListStoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(private val binding: ItemMystoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imgItemPhoto)

            binding.tvItemName.text = story.name

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, DetailStoryActivity::class.java).apply {
                    putExtra(DetailStoryActivity.ID, story.id)
                }
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context as Activity,
                    Pair(binding.imgItemPhoto, "profile_image"),
                    Pair(binding.tvItemName, "name")
                )
                context.startActivity(intent, options.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMystoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}




