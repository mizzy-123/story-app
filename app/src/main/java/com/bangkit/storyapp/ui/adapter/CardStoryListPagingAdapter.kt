package com.bangkit.storyapp.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.storyapp.data.model.response.GetStoriesResponse
import com.bangkit.storyapp.data.model.response.ListStory
import com.bangkit.storyapp.databinding.ItemListCardStoryBinding
import com.bangkit.storyapp.ui.activities.DetailStoryActivity
import com.bumptech.glide.Glide

class CardStoryListPagingAdapter : PagingDataAdapter<ListStory, CardStoryListPagingAdapter.MyViewHolder>(
    DIFF_CALLBACK) {

    inner class MyViewHolder(var binding: ItemListCardStoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)

        if (story != null){
            Glide.with(holder.itemView.context)
                .load(story.photoUrl)
                .into(holder.binding.imgStory)
            holder.binding.judul.text = story.name
            holder.binding.description.text = story.description

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.EXTRA_ID, story.id)

                val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(holder.binding.imgStory, "profile"),
                    Pair(holder.binding.judul, "title"),
                    Pair(holder.binding.description, "description")
                )

                holder.itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListCardStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStory>() {
            override fun areItemsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}