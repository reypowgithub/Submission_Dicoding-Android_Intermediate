package com.example.caritaappnew.view.main.list

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caritaappnew.R
import com.example.caritaappnew.databinding.ItemRowStoryBinding
import com.example.caritaappnew.model.DiffUtils
import com.example.caritaappnew.model.respon.Stories
import com.example.caritaappnew.view.main.detail.DetailActivity


class StoryAdapter: PagingDataAdapter<Stories, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }

    inner class ViewHolder(private val binding: ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listStory : Stories){
            with(binding){
                Glide.with(photoStory)
                    .load(listStory.photoUrl)
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_broken_image)
                    .into(binding.photoStory)
                tvName.text = listStory.name
                tvDate.text= binding.root.resources.getString(R.string.create_date, listStory.createdAt)

                binding.photoStory.setOnClickListener {
                    val intent = Intent(it.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_STORY, listStory)
                    it.context.startActivity(intent)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Stories>() {
            override fun areItemsTheSame(oldItem: Stories, newItem: Stories): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Stories, newItem: Stories): Boolean {
                return oldItem == newItem
            }
        }
    }
}