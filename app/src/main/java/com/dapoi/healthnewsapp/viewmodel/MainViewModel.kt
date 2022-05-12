package com.dapoi.healthnewsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dapoi.healthnewsapp.data.source.NewsRepository

class MainViewModel(newsRepository: NewsRepository) : ViewModel() {
    val news = newsRepository.getNews().cachedIn(viewModelScope)
}