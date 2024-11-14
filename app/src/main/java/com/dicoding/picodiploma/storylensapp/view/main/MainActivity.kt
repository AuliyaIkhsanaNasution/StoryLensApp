package com.dicoding.picodiploma.storylensapp.view.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.storylensapp.data.api.ApiConfig
import com.dicoding.picodiploma.storylensapp.databinding.ActivityMainBinding
import com.dicoding.picodiploma.storylensapp.view.ViewModelFactory
import com.dicoding.picodiploma.storylensapp.view.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this, ApiConfig().getApiService("token"))
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        // Memanggil fungsi untuk mengambil cerita
        lifecycleScope.launch {
            viewModel.getStories()
        }

        // Mengamati perubahan pada storyList
        viewModel.storyList.observe(this) { stories ->
            if (stories != null) {
                mainAdapter.setStories(stories) // Set data ke adapter
            }
            binding.progressBar.visibility = View.GONE
        }


        mainAdapter = MainAdapter()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.adapter = mainAdapter
    }


}