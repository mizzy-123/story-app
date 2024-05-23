package com.bangkit.storyapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bangkit.storyapp.data.model.response.ListStory
import com.bangkit.storyapp.data.repository.StoryRepository

class StoryPagingViewModel(storyRepository: StoryRepository) : ViewModel() {
    val story: LiveData<PagingData<ListStory>> = storyRepository.getStory().cachedIn(viewModelScope)
}