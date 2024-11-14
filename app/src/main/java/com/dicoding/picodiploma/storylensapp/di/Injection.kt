package com.dicoding.picodiploma.storylensapp.di

import android.content.Context
import com.dicoding.picodiploma.storylensapp.data.api.ApiService
import com.dicoding.picodiploma.storylensapp.data.repository.UserRepository
import com.dicoding.picodiploma.storylensapp.data.pref.UserPreference
import com.dicoding.picodiploma.storylensapp.data.pref.dataStore

object Injection {

    fun userRepositoryProvide(context: Context, apiService: ApiService): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref,apiService)
    }
}