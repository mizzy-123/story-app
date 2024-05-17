package com.bangkit.storyapp.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bangkit.storyapp.R
import com.bangkit.storyapp.databinding.ActivityDetailStoryBinding
import com.bangkit.storyapp.ui.viewmodels.StoryViewModel
import com.bumptech.glide.Glide

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    var id = ""
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        initLayout()
        setAction()
    }

    private fun initComponents(){
        pref = getSharedPreferences("userpref", Context.MODE_PRIVATE)
        val bundle: Bundle? = intent.extras
        if (bundle != null){
            id = bundle.getString(EXTRA_ID, "")
        }
        storyViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(StoryViewModel::class.java)
    }

    private fun initLayout(){
        val token = pref.getString("token", "").toString()
        storyViewModel.getDetailStories(this@DetailStoryActivity, token, id){
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setAction(){

        storyViewModel.loadingGetDetailStory.observe(this){
            if (it){
                binding.imgDetail.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_place_holder))
                binding.tvJudul.text = resources.getString(R.string.loading)
                binding.tvDescription.text = resources.getString(R.string.loading)
            }
        }

        storyViewModel.getDetailStory.observe(this){
            Glide.with(this)
                .load(it.photoUrl)
                .into(binding.imgDetail)
            binding.tvJudul.text = it.name
            binding.tvDescription.text = it.description
        }
    }

    companion object {
        const val EXTRA_ID = "id"
    }
}