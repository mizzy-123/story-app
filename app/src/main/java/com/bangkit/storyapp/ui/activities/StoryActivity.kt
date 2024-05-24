package com.bangkit.storyapp.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bangkit.storyapp.R
import com.bangkit.storyapp.data.model.response.ListStory
import com.bangkit.storyapp.databinding.ActivityStoryBinding
import com.bangkit.storyapp.ui.adapter.CardListStoryAdapter
import com.bangkit.storyapp.ui.adapter.CardStoryListPagingAdapter
import com.bangkit.storyapp.ui.adapter.LoadingStoryListPagingAdapter
import com.bangkit.storyapp.ui.viewmodels.StoryPagingViewModel
import com.bangkit.storyapp.ui.viewmodels.StoryViewModel
import com.bangkit.storyapp.ui.viewmodels.ViewModelFactory

class StoryActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityStoryBinding
    private lateinit var listStory: ArrayList<ListStory>
    private lateinit var storyPagingViewModel: StoryPagingViewModel
    private lateinit var cardStoryListPagingAdapter: CardStoryListPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        setupAction()
        setupSwipeRefreshLayout()
        showRecycleCardList()
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipe.setOnRefreshListener(this)
    }

    private fun setupAction() {
        binding.fab.setOnClickListener {
            val intent = Intent(this@StoryActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }

        binding.toolBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.logout -> {
                    val sharedPreferences = getSharedPreferences("userpref", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("token", null)
                    editor.apply()
                    finishAffinity()
                    true
                }
                R.id.location -> {
                    val intent = Intent(this@StoryActivity, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun initComponents(){
        storyPagingViewModel = obtainStoryPagingViewModel()
        listStory = ArrayList()
    }

    private fun showRecycleCardList(){
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        cardStoryListPagingAdapter = CardStoryListPagingAdapter()
        binding.rvStory.adapter = cardStoryListPagingAdapter.withLoadStateFooter(
            footer = LoadingStoryListPagingAdapter {
                cardStoryListPagingAdapter.retry()
            }
        )

        storyPagingViewModel.story.observe(this){
            Log.d("StoryActivity", "New data received")
            cardStoryListPagingAdapter.submitData(lifecycle, it)

            binding.swipe.isRefreshing = false

            // Scroll ke posisi paling atas setelah data berhasil dimuat
            binding.rvStory.scrollToPosition(0)
        }

        // Log untuk melihat apakah ada error dalam load state
        cardStoryListPagingAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Error || loadState.append is LoadState.Error) {
                // Ada error saat memuat data
                Log.d("StoryActivity", "Error refresh")
                binding.swipe.isRefreshing = false
            }
        }
    }

    private fun obtainStoryPagingViewModel(): StoryPagingViewModel {
        val sharedPreferences = getSharedPreferences("userpref", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")
        val factory = ViewModelFactory(this@StoryActivity, token!!)
        return ViewModelProvider(this, factory).get(StoryPagingViewModel::class.java)
    }

    override fun onRefresh() {
        Log.d("StoryActivity", "Refreshing data")
        cardStoryListPagingAdapter.refresh()
    }
}