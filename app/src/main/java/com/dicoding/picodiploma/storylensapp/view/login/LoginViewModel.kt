package com.dicoding.picodiploma.storylensapp.view.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.storylensapp.data.repository.UserRepository
import com.dicoding.picodiploma.storylensapp.data.pref.UserModel
import com.dicoding.picodiploma.storylensapp.data.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    suspend fun login(email: String, password: String): LoginResponse {
        return try {
            Log.d("LoginViewModel", "Initiating login request for email: $email")
            val response = repository.login(email, password)
            Log.d("LoginViewModel", "Login response received: $response")

            if (response.loginResult != null) {
                response.loginResult.token?.let { token ->
                    val user = UserModel(email, token, true)
                    saveSession(user)
                }
            }
            response
        } catch (e: Exception) {
            Log.e("LoginViewModel", "An error occurred during login: ${e.message}")
            throw e
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}