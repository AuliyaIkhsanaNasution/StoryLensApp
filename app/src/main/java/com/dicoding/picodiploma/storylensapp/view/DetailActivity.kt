package com.dicoding.picodiploma.storylensapp.view

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.storylensapp.data.api.ApiConfig
import com.dicoding.picodiploma.storylensapp.data.response.DetailtStoryResponse
import com.dicoding.picodiploma.storylensapp.databinding.ActivityDetailBinding
import com.dicoding.picodiploma.storylensapp.view.main.MainViewModel
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this, ApiConfig().getApiService("token"))
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyId = intent.getStringExtra("STORY_ID") ?: ""
        if (storyId.isNotEmpty()) {
            fetchStoryDetails(storyId)
        } else {
            Log.e("DetailActivity", "Story ID not found")
        }
    }

    private fun fetchStoryDetails(storyId: String) {
        lifecycleScope.launch {
            try {
                val response = viewModel.getStoryById(storyId)
                updateUI(response)
            } catch (e: Exception) {
                Log.e("DetailActivity", "Error fetching story details: ${e.message}", e)
            }
        }
    }

    private fun updateUI(storyResponse: DetailtStoryResponse) {
        val story = storyResponse.story
        if (story != null) {
            binding.apply {
                tvTitle.text = story.name
                tvDesc.text = story.description
                tvCreateat.text = story.createdAt

                Glide.with(this@DetailActivity)
                    .load(story.photoUrl)
                    .into(imgDetail)
            }
        } else {
            Log.e("DetailActivity", "Story details not found")
        }
    }
}