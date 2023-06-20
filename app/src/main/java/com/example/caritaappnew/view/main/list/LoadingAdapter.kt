package com.example.caritaappnew.view.main.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.caritaappnew.databinding.ItemLoadingBinding

class LoadingAdapter(private val retry : () -> Unit)
    : LoadStateAdapter<LoadingAdapter.LoadingViewHolder>() {

    override fun onBindViewHolder(holder: LoadingViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingViewHolder {
        val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return LoadingViewHolder(binding, retry)
    }

    class LoadingViewHolder (private val binding : ItemLoadingBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

            init {
                binding.retryButton.setOnClickListener { retry.invoke() }
            }

        fun bind(loadState: LoadState){
            if (loadState is LoadState.Error){
                binding.error.text = loadState.error.localizedMessage
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            binding.error.isVisible = loadState is LoadState.Error
        }
    }


}