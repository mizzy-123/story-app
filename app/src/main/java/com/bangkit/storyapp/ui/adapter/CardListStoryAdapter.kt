package com.bangkit.storyapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.storyapp.data.model.response.ListStory
import com.bangkit.storyapp.databinding.ItemListCardStoryBinding
import com.bumptech.glide.Glide

class CardListStoryAdapter(private val stories: ArrayList<ListStory>) :
 RecyclerView.Adapter<CardListStoryAdapter.ListViewHolder>() {

    inner class ListViewHolder(var binding: ItemListCardStoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListCardStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return stories.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = stories[position]
        Glide.with(holder.itemView.context)
            .load(story.photoUrl)
            .into(holder.binding.imgStory)
        holder.binding.judul.text = story.name
        holder.binding.description.text = story.description
    }
}