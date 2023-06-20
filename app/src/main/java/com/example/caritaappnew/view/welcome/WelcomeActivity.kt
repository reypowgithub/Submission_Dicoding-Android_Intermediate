package com.example.caritaappnew.view.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import com.example.caritaappnew.R
import com.example.caritaappnew.databinding.ActivityWelcomeBinding
import com.example.caritaappnew.view.login.LoginActivity
import com.example.caritaappnew.view.singup.SingUpActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            loginButton.setOnClickListener {
                val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
                startActivity(intent)
            }

            signinButton.setOnClickListener {
                val intent = Intent(this@WelcomeActivity, SingUpActivity::class.java)
                startActivity(intent)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.tb_languange -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else -> {return super.onOptionsItemSelected(item)}
        }
    }
}