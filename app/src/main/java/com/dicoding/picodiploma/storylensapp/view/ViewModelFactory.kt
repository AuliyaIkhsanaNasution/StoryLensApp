package com.dicoding.picodiploma.storylensapp.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.storylensapp.data.api.ApiService
import com.dicoding.picodiploma.storylensapp.data.repository.ListStoryRepository
import com.dicoding.picodiploma.storylensapp.data.repository.UserRepository
import com.dicoding.picodiploma.storylensapp.di.Injection
import com.dicoding.picodiploma.storylensapp.view.login.LoginViewModel
import com.dicoding.picodiploma.storylensapp.view.main.MainViewModel
import com.dicoding.picodiploma.storylensapp.view.signup.SignupViewModel

class ViewModelFactory(private val repository: UserRepository,
                       private val storyRepository: ListStoryRepository,) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(repository,storyRepository) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(repository) as T
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> SignupViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context, apiService: ApiService): ViewModelFactory {
            clearInstance()

            synchronized(ViewModelFactory::class.java) {
                val userRepository = Injection.userRepositoryProvide(context, apiService)
                val storyRepository = Injection.provideStoryRepository(context)
                INSTANCE = ViewModelFactory(userRepository,storyRepository )

            }

            return INSTANCE as ViewModelFactory
        }

        @JvmStatic
        private fun clearInstance() {
            INSTANCE = null
        }
    }
}