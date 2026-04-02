package com.ceph.pulsepoint.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ceph.pulsepoint.domain.model.Article
import com.ceph.pulsepoint.domain.repository.PulseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: PulseRepository
) : ViewModel() {

    private val _articles = MutableStateFlow<PagingData<Article>>(PagingData.empty())
    val articles = _articles.asStateFlow()

    private val _categoryNews = MutableStateFlow<PagingData<Article>>(PagingData.empty())
    val categoryNews = _categoryNews.asStateFlow()

    init {

        getNewsHeadlines()
    }

    fun getNewsHeadlines() {
        viewModelScope.launch {
            repository.getNewsHeadlines().collect {
                _articles.value = it
            }

        }
    }

    fun getNewsByCategory(category: String) {
        viewModelScope.launch {
            repository.getNewsByCategory(category)
                .collect {
                    _categoryNews.value = it
                }
        }
    }


    val favoriteArticlesUrls: StateFlow<List<String>> = repository.getFavoriteArticlesUrls()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun toggleFavoriteStatus(article: Article) {

        viewModelScope.launch {
            try {
                repository.toggleFavoriteStatus(article)
            } catch (e: Exception) {
                Log.e("FavoriteViewModel", "Error toggling favorite status: ${e.message}")
                e.printStackTrace()
            }

        }
    }

}