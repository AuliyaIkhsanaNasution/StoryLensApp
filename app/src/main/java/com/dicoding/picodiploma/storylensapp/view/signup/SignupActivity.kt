package com.dicoding.picodiploma.storylensapp.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.storylensapp.data.api.ApiConfig
import com.dicoding.picodiploma.storylensapp.data.repository.UserRepository
import com.dicoding.picodiploma.storylensapp.databinding.ActivitySignupBinding
import com.dicoding.picodiploma.storylensapp.di.Injection
import com.dicoding.picodiploma.storylensapp.view.ViewModelFactory
import com.dicoding.picodiploma.storylensapp.view.login.LoginActivity
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this, ApiConfig().getApiService("token"))
    }

    private lateinit var binding: ActivitySignupBinding
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userRepository = Injection.userRepositoryProvide(this, ApiConfig().getApiService("token"))

        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            Log.d("SignupActivity", "Button Daftar ditekan")

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                Log.d(
                    "SignupActivity",
                    "Data lengkap: Name=$name, Email=$email, Password=$password"
                )
                binding.progressBar.visibility = View.VISIBLE
                binding.signupButton.visibility = View.INVISIBLE

                lifecycleScope.launch {
                    try {
                        val response = viewModel.register(name, email, password)
                        Log.d("SignupActivity", "Percobaan registrasi selesai")

                        val dialogTitle =
                            if (!response.error!!) "Welcome!" else if (response.message == "Email is already taken") "Email has been used" else "Failed registration"
                        val dialogMessage =
                            if (!response.error) "Your account $email is successfully created." else if (response.message == "Email is already taken") "$email has been used. Try another." else response.message
                                ?: "Please try again later."

                        val isError = response.error
                        showDialog(dialogTitle, dialogMessage, isError)

                    } catch (e: Exception) {
                        Log.e("SignupActivity", "Gagal melakukan registrasi", e)
                        showDialog("Failed registration", "Please try again later.", true)
                    } finally {
                        binding.progressBar.visibility = View.GONE
                        binding.signupButton.visibility = View.VISIBLE
                    }
                }
            } else {
                Log.w("SignupActivity", "Data is not complete, yet.")
                showDialog("Failed registration", "Please fill out the required data.", true)
            }
        }
    }

    private fun showDialog(title: String, message: String, isError: Boolean) {
        if (!isFinishing) { // Memastikan aktivitas belum dihancurkan
            AlertDialog.Builder(this@SignupActivity).apply {
                setTitle(title)
                setMessage(message)
                setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    if (!isError) {
                        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                create()
                show()
            }
        } else {
            Log.w("SignupActivity", "Activity is finishing, dialog not shown.")
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(200)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(200)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(200)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(200)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(200)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }
}