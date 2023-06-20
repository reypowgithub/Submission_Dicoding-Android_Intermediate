package com.example.caritaappnew.view.welcome

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.caritaappnew.R
import com.example.caritaappnew.model.userPreference
import com.example.caritaappnew.view.main.MainActivity
import com.example.caritaappnew.view.main.MainFactory
import com.example.caritaappnew.view.main.MainViewModel

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "settings")
class SplashScreen : AppCompatActivity() {

    private lateinit var mainViewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val mainFactory = MainFactory(userPreference.getInstance(dataStore))
        mainViewModel = ViewModelProvider(this,mainFactory)[MainViewModel::class.java]

        supportActionBar?.hide()

        mainViewModel.getUser().observe(this){
            if (it.isLogin){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this,WelcomeActivity::class.java))
                finish()
            }

        }
    }

}