package com.dapoi.healthnewsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dapoi.healthnewsapp.databinding.ItemLoadingStateBinding

class LoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadingStateAdapter.LoadingViewHolder>() {

    inner class LoadingViewHolder(
        private val binding: ItemLoadingStateBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(load: LoadState) {
            if (load is LoadState.Error) {
                binding.errorMsg.text = load.error.localizedMessage
            }
            binding.apply {
                progressBar.isVisible = load is LoadState.Loading
                retryButton.isVisible = load is LoadState.Error
                errorMsg.isVisible = load is LoadState.Error
            }
        }
    }

    override fun onBindViewHolder(holder: LoadingViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingViewHolder {
        return LoadingViewHolder(
            ItemLoadingStateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            retry
        )
    }
}