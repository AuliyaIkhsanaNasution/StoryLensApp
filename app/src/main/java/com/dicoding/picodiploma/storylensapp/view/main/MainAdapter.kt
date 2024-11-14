package com.dicoding.picodiploma.storylensapp.view.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.storylensapp.R
import com.dicoding.picodiploma.storylensapp.data.response.ListStoryItem

class MainAdapter : RecyclerView.Adapter<MainAdapter.StoryViewHolder>() {

    // Menyimpan daftar item cerita
    private val stories = mutableListOf<ListStoryItem>()

    // Menginisialisasi ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return StoryViewHolder(view)
    }

    // Mengikat data ke dalam ViewHolder
    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount(): Int = stories.size

    // Memperbarui data dalam adapter
    @SuppressLint("NotifyDataSetChanged")
    fun setStories(newStories: List<ListStoryItem>) {
        stories.clear()
        stories.addAll(newStories)
        notifyDataSetChanged()
    }

    inner class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgItemPhoto: ImageView = itemView.findViewById(R.id.img_story)
        private val tvItemName: TextView = itemView.findViewById(R.id.tv_title)
        private val tvItemDescription: TextView = itemView.findViewById(R.id.tv_desc)
        private val tvCreatedAt: TextView = itemView.findViewById(R.id.tv_createat)

        fun bind(story: ListStoryItem) {
            Glide.with(itemView)
                .load(story.photoUrl)
                .into(imgItemPhoto)

            tvItemName.text = story.name
            tvItemDescription.text = story.description
            tvCreatedAt.text = story.createdAt


        }
    }
}
