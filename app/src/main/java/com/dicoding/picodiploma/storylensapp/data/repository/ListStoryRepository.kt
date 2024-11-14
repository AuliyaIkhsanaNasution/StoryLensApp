package com.dicoding.picodiploma.storylensapp.data.repository

import com.dicoding.picodiploma.storylensapp.data.api.ApiService
import com.dicoding.picodiploma.storylensapp.data.pref.UserPreference
import com.dicoding.picodiploma.storylensapp.data.response.DetailtStoryResponse
import com.dicoding.picodiploma.storylensapp.data.response.ListStoryResponse
import kotlinx.coroutines.flow.firstOrNull

class ListStoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    companion object {
        fun getInstance(apiService: ApiService, userPreference: UserPreference): ListStoryRepository {
            return ListStoryRepository(apiService, userPreference)
        }
    }


    suspend fun getStories(): ListStoryResponse {
        try {
            userPreference.getSession().firstOrNull()?.token
                ?: throw NullPointerException("Token is null")
            return apiService.getListStory()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getStoryById(storyId: String): DetailtStoryResponse {
        try {
            userPreference.getSession().firstOrNull()?.token
                ?: throw NullPointerException("Token is null")
            return apiService.getDetailStory(storyId)
        } catch (e: Exception) {
            throw e
        }
    }
}