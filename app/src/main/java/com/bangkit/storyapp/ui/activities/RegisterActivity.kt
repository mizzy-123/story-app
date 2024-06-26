package com.bangkit.storyapp.ui.activities

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bangkit.storyapp.R
import com.bangkit.storyapp.databinding.ActivityRegisterBinding
import com.bangkit.storyapp.ui.viewmodels.AccountViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var accountViewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        setupView()
        setupAction()
        playAnimation()

    }

    private fun initComponents() {
        accountViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            AccountViewModel::class.java)
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
            val username: String = binding.nameEditText.text.toString()
            val email: String = binding.emailEditText.text.toString()
            val password: String = binding.passwordEditText.text.toString()

            accountViewModel.register(
                this@RegisterActivity,
                username,
                email,
                password
            )
        }

        val pDialogLoading = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialogLoading.titleText = "Loading..."
        pDialogLoading.progressHelper.barColor = Color.parseColor("#2D3D4F")
        pDialogLoading.setCancelable(false)

        accountViewModel.loadingRegister.observe(this){

            if (it){
                pDialogLoading.show()
            } else {
                pDialogLoading.dismiss()
            }
        }

        val pDialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        pDialog.setTitleText("Success")
        pDialog.setContentText("Account has been created")
        pDialog.setConfirmText("Alright")
        pDialog.setConfirmClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        accountViewModel.isAccountCreated.observe(this){

            if (it){
                pDialog.show()
            } else {
                pDialog.dismiss()
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)


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