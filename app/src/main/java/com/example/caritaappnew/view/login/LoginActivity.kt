package com.example.caritaappnew.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.caritaappnew.R
import com.example.caritaappnew.databinding.ActivityLoginBinding
import com.example.caritaappnew.model.ApiCallback
import com.example.caritaappnew.model.userPreference
import com.example.caritaappnew.view.main.MainActivity
import com.example.caritaappnew.view.singup.SingUpActivity

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var loginViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = ViewModelProvider(this,
            LoginFactory(userPreference.getInstance(dataStore)))[LoginViewModel::class.java]

        setButtonEnable()
        editTextListener()
        buttonActivity()
        playAnimation()
        binding.ProgressBarLogin.visibility = View.GONE
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.textView3, View.ALPHA, 1f).setDuration(500)
        val emailtv = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val emailtl = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passtv = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val passtl = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val loginBtn = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, emailtv, emailtl, passtv, passtl,loginBtn)
            startDelay = 500
        }.start()
    }

    private fun editTextListener(){
        binding.apply {
            binding.ProgressBarLogin.visibility = View.GONE

            edLoginEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    setButtonEnable()
                }

                override fun afterTextChanged(s: Editable?) {
                }

            })

            edLoginPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    setButtonEnable()
                }

                override fun afterTextChanged(s: Editable?) {
                }

            })
        }
    }

    private fun buttonActivity(){
        binding.apply {
            chooseSingup.setOnClickListener {
                startActivity(Intent(this@LoginActivity, SingUpActivity::class.java))
                finish()
            }

            loginButton.setOnClickListener {
                val email = binding.edLoginEmail.text.toString()
                val password = binding.edLoginPassword.text.toString()

                loginViewModel.loginUser(email, password, object : ApiCallback {
                    override fun onResponse(success: Boolean, message: String) {
                        if(success){
                            AlertDialog.Builder(this@LoginActivity).apply {
                                setTitle(getString(R.string.information))
                                setMessage(getString(R.string.success_login))
                                setPositiveButton(getString(R.string.next)) {_, _ ->
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                        } else {
                            AlertDialog.Builder(this@LoginActivity).apply {
                                setTitle(getString(R.string.information))
                                setMessage(getString(R.string.login_failed) + ", $message")
                                setPositiveButton(this@LoginActivity.getString(R.string.next)) { _, _ ->
                                    ProgressBarLogin.visibility = View.GONE
                                }
                                create()
                                show()
                            }
                        }
                    }

                })
            }
        }
    }

    private fun setButtonEnable(){
        binding.apply {
            loginButton.isEnabled = edLoginPassword.text != null && edLoginEmail.text != null &&
                    edLoginPassword.text.toString().length >= 8 &&
                    Patterns.EMAIL_ADDRESS.matcher(edLoginEmail.text.toString()).matches()
        }
    }
}