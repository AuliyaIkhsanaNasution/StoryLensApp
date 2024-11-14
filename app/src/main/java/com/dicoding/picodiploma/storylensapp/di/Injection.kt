package com.dicoding.picodiploma.storylensapp.di

import android.content.Context
import com.dicoding.picodiploma.storylensapp.data.api.ApiConfig
import com.dicoding.picodiploma.storylensapp.data.api.ApiService
import com.dicoding.picodiploma.storylensapp.data.repository.UserRepository
import com.dicoding.picodiploma.storylensapp.data.pref.UserPreference
import com.dicoding.picodiploma.storylensapp.data.pref.dataStore
import com.dicoding.picodiploma.storylensapp.data.repository.ListStoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun userRepositoryProvide(context: Context, apiService: ApiService): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref,apiService)
    }

    fun provideStoryRepository(context: Context): ListStoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig().getApiService(user.token)
        return ListStoryRepository.getInstance(apiService, pref)
    }
}