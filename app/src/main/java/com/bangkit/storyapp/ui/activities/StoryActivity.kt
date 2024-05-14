package com.bangkit.storyapp.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.storyapp.R
import com.bangkit.storyapp.data.model.response.ListStory
import com.bangkit.storyapp.databinding.ActivityStoryBinding
import com.bangkit.storyapp.ui.adapter.CardListStoryAdapter
import com.bangkit.storyapp.ui.viewmodels.StoryViewModel

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding
    private lateinit var listStory: ArrayList<ListStory>
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var cardListStoryAdapter: CardListStoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        setupAction()
    }

    override fun onResume() {
        super.onResume()
        showRecycleCardList()
    }

    private fun setupAction() {
        binding.fab.setOnClickListener {
            val intent = Intent(this@StoryActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initComponents(){
        listStory = ArrayList()
        storyViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(StoryViewModel::class.java)
    }

    private fun showRecycleCardList(){
        val sharedPreferences = getSharedPreferences("userpref", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        binding.rvStory.layoutManager = LinearLayoutManager(this)

        binding.rvStory.visibility = View.INVISIBLE
        binding.loading.visibility = View.VISIBLE
        if (token != null){
            storyViewModel.getStories(this@StoryActivity, token){
                if (it != null){
                    listStory.clear()
                    listStory.addAll(it)
                    cardListStoryAdapter = CardListStoryAdapter(listStory)
                    binding.rvStory.adapter = cardListStoryAdapter
                }

                binding.rvStory.visibility = View.VISIBLE
                binding.loading.visibility = View.INVISIBLE
            }
        }
    }
}