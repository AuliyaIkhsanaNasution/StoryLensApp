package com.dicoding.picodiploma.storylensapp.view.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.storylensapp.data.repository.UserRepository
import com.dicoding.picodiploma.storylensapp.data.response.RegisterResponse
import retrofit2.HttpException

class SignupViewModel(private val repository: UserRepository) : ViewModel() {

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return try {
            Log.d(
                "SignupViewModel",
                "Mengirim permintaan registrasi ke server"
            ) // Pesan log ini akan muncul saat permintaan registrasi dikirim.
            val response = repository.register(name, email, password)

            Log.d(
                "SignupViewModel",
                "Registrasi berhasil"
            ) // Pesan log ini akan muncul jika registrasi berhasil.
            response
        } catch (e: HttpException) {
            Log.e(
                "SignupViewModel",
                "Registrasi gagal",
                e
            ) // Pesan log ini akan muncul jika registrasi gagal.

            val errorBody = e.response()?.errorBody()?.string()
            Log.e("SignupViewModel", "Error response body: $errorBody")
            throw e
        }
    }
}
