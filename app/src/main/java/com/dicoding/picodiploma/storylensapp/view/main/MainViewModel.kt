package com.dicoding.picodiploma.storylensapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.storylensapp.data.repository.UserRepository
import com.dicoding.picodiploma.storylensapp.data.pref.UserModel
import com.dicoding.picodiploma.storylensapp.data.repository.ListStoryRepository
import com.dicoding.picodiploma.storylensapp.data.response.DetailtStoryResponse
import com.dicoding.picodiploma.storylensapp.data.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository,
                    private val storyRepository: ListStoryRepository) : ViewModel() {

    private val _storyList = MutableLiveData<List<ListStoryItem>>()
    // Public getter untuk LiveData
    val storyList: LiveData<List<ListStoryItem>> get() = _storyList

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    suspend fun getStories() {
        try {
            val response = storyRepository.getStories()
            _storyList.postValue(response.listStory)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getStoryById(storyId: String): DetailtStoryResponse {
        try {
            return storyRepository.getStoryById(storyId)
        } catch (e: Exception) {
            throw e
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}