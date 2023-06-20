package com.example.caritaappnew.view.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.caritaappnew.R
import com.example.caritaappnew.databinding.ActivityMainBinding
import com.example.caritaappnew.model.UserModel
import com.example.caritaappnew.model.userPreference
import com.example.caritaappnew.view.login.LoginActivity
import com.example.caritaappnew.view.main.list.ListActivity
import com.example.caritaappnew.view.welcome.SplashScreen

val Context.dataStore : DataStore<Preferences> by preferencesDataStore("settings")
class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding : ActivityMainBinding
    private lateinit var user : UserModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        buttonListener()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            MainFactory(userPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this){
            user = UserModel(
                it.name,
                it.email,
                it.password,
                it.userId,
                it.token,
                true
            )
            binding.mainText1.text = getString(R.string.greetings, user.name)
        }
    }

    private fun buttonListener(){
        binding.apply {
            contineButton.setOnClickListener {
                val startListActivity = Intent(this@MainActivity, ListActivity::class.java)
                startActivity(startListActivity.putExtra(ListActivity.EXTRA_USER, user))
            }
            logoutButton.setOnClickListener {
                mainViewModel.logout()
                AlertDialog.Builder(this@MainActivity).apply {
                    setTitle(getString(R.string.information))
                    setMessage(getString(R.string.success_logout))
                    setPositiveButton(getString(R.string.next)){_,_ ->
                        val intent = Intent(this@MainActivity, SplashScreen::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        finish()
                    }
                    create()
                    show()
                }
            }
        }
    }






}