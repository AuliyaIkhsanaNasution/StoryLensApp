package com.dicoding.picodiploma.storylensapp.data.repository

import android.util.Log
import com.dicoding.picodiploma.storylensapp.data.api.ApiService
import com.dicoding.picodiploma.storylensapp.data.pref.UserModel
import com.dicoding.picodiploma.storylensapp.data.pref.UserPreference
import com.dicoding.picodiploma.storylensapp.data.response.LoginResponse
import com.dicoding.picodiploma.storylensapp.data.response.RegisterResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return try {
            val response = apiService.register(name, email, password)
            Log.d("UserRepository", "Registration successful. Response: $response")
            response
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("UserRepository", "Registration failed. Error body: $errorBody")
            throw e
        }
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return try {
            val response = apiService.login(email, password)
            Log.d("UserRepository", "Login successful. Response: $response")
            response
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("UserRepository", "Login failed. Error body: $errorBody")
            throw e
        }
    }


    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository( userPreference ,apiService)
            }.also { instance = it }
    }
}