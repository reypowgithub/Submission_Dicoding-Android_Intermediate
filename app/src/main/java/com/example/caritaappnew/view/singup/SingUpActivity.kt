package com.example.caritaappnew.view.singup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.caritaappnew.R
import com.example.caritaappnew.databinding.ActivitySingUpBinding
import com.example.caritaappnew.model.ApiCallback
import com.example.caritaappnew.view.login.LoginActivity

class SingUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingUpBinding
    private lateinit var viewModel : SingUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySingUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(SingUpViewModel::class.java)

        setButtonEnable()
        editTextListener()
        buttonActivity()
        playAnimation()
        binding.progressBarRegister.visibility = View.GONE
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.textView2, View.ALPHA, 1f).setDuration(500)
        val nametv = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(500)
        val nametl = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val emailtv = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val emailtl = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passtv = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val passtl = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val signinBtn = ObjectAnimator.ofFloat(binding.signinButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, nametv, nametl, emailtv, emailtl, passtv, passtl,signinBtn)
            startDelay = 500
        }.start()
    }

    private fun editTextListener(){
        binding.apply {
            progressBarRegister.visibility = View.GONE

            edRegisterName.addTextChangedListener(object : TextWatcher {
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

            edRegisterEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    setButtonEnable()
                }

                override fun afterTextChanged(s: Editable?) {
                }

            })

            edRegisterPassword.addTextChangedListener(object : TextWatcher {
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
            signinButton.setOnClickListener {
                val name = binding.edRegisterName.text.toString()
                val email = binding.edRegisterEmail.text.toString()
                val password = binding.edRegisterPassword.text.toString()

                viewModel.singupUser(name,email,password, object : ApiCallback {
                    override fun onResponse(success: Boolean, message: String) {
                        if(success){
                            AlertDialog.Builder(this@SingUpActivity).apply {
                                setTitle(getString(R.string.information))
                                setMessage(getString(R.string.success_singup))
                                setPositiveButton(getString(R.string.next)) {_, _ ->
                                    val intent = Intent(this@SingUpActivity, LoginActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                        } else {
                            AlertDialog.Builder(this@SingUpActivity).apply {
                                setTitle(getString(R.string.information))
                                setMessage(getString(R.string.register_failed) + ", $message")
                                setPositiveButton(this@SingUpActivity.getString(R.string.next)) { _, _ ->
                                    progressBarRegister.visibility = View.GONE
                                }
                                create()
                                show()
                            }
                        }
                    }
                })
            }

            chooseLogin.setOnClickListener{
                startActivity(Intent(this@SingUpActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun setButtonEnable(){
        binding.apply {
            signinButton.isEnabled =
                edRegisterEmail.text.toString().isNotEmpty() &&
                        edRegisterPassword.text.toString().isNotEmpty() &&
                        edRegisterPassword.text.toString().length >= 8 &&
                        Patterns.EMAIL_ADDRESS.matcher(edRegisterEmail.text.toString()).matches()
        }
    }

}