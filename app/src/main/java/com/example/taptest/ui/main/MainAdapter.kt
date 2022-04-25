package com.example.taptest.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.taptest.databinding.ItemFrameBinding

class MainAdapter(
    diffUtil: MainDiffUtil = MainDiffUtil()
) : PagingDataAdapter<String, MainViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(ItemFrameBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}

class MainViewHolder(
    private val binding: ItemFrameBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(frameUrl: String) {
        Glide.with(itemView.context)
            .load(frameUrl)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(binding.imageView)
    }
}

class MainDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}
