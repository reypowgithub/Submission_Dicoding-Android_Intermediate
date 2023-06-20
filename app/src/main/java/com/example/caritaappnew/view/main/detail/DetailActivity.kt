package com.example.caritaappnew.view.main.detail

import android.graphics.text.LineBreaker
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.caritaappnew.R
import com.example.caritaappnew.databinding.ActivityDetailBinding
import com.example.caritaappnew.model.respon.Stories

class DetailActivity : AppCompatActivity() {
    private lateinit var story : Stories
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    companion object {
        const val EXTRA_STORY = "story"
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        run {
            binding.tvDetailDesc.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
        }

        @Suppress("DEPRECATION")
        story = intent.getParcelableExtra(EXTRA_STORY)!!
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.detailStory(story)
        updateData()

    }

    override fun onSupportNavigateUp(): Boolean {
        @Suppress("DEPRECATION")
        onBackPressed()
        return true
    }

    private fun updateData(){
        with(binding){
            tvDetailName.text = viewModel.itemStory.name
            tvDetailDate.text = getString(R.string.create_date, viewModel.itemStory.createdAt)
            tvDetailDesc.text = viewModel.itemStory.description

            Glide.with(ivDetailPreview)
                .load(viewModel.itemStory.photoUrl)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_broken_image)
                .into(ivDetailPreview)
        }
    }
}