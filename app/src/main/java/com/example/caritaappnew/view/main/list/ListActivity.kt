package com.example.caritaappnew.view.main.list

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.caritaappnew.databinding.ActivityListBinding
import com.example.caritaappnew.R
import com.example.caritaappnew.model.UserModel
import com.example.caritaappnew.model.userPreference
import com.example.caritaappnew.view.main.detail.DetailActivity
import com.example.caritaappnew.view.main.maps.MapsActivity
import com.example.caritaappnew.view.main.story.AddStoryActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")

class ListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityListBinding
    private lateinit var user: UserModel

    private val listViewModel: ListViewModel by viewModels {
        ListFactory(this, userPreference.getInstance(dataStore))
    }

    companion object {
        const val EXTRA_USER = "user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.addStory.setOnClickListener {
            val moveAddStoryActivity = Intent(this, AddStoryActivity::class.java)
            moveAddStoryActivity.putExtra(AddStoryActivity.EXTRA_USER, user)
            startActivity(moveAddStoryActivity)
        }
        @Suppress("DEPRECATION")
        user = intent.getParcelableExtra(EXTRA_USER)!!


        getList()
    }


    private fun getList() {
        val adapter = StoryAdapter()
        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager
        binding.rvStories.setHasFixedSize(true)
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingAdapter {
                adapter.retry()
            }
        )
        listViewModel.story(user.token).observe(this) {
            adapter.submitData(lifecycle, it)
        }
        showLoad(false)
        binding.rvStories.setOnClickListener { Stories ->
            val moveDetailActivity = Intent(this, DetailActivity::class.java)
            moveDetailActivity.putExtra(DetailActivity.EXTRA_STORY, Stories as Parcelable)
            startActivity(moveDetailActivity)
        }
    }

    private fun showLoad(loadingstatus: Boolean) {
        if (loadingstatus) {
            binding.rvStories.visibility = View.GONE
            binding.progressBarMain.visibility = View.VISIBLE
            binding.tvInfo.visibility = View.GONE
        } else {
            binding.rvStories.visibility = View.VISIBLE
            binding.progressBarMain.visibility = View.GONE
            binding.tvInfo.visibility = View.GONE
        }
    }



    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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
            R.id.tb_map -> {
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }
            else -> {return super.onOptionsItemSelected(item)}
        }
    }
}