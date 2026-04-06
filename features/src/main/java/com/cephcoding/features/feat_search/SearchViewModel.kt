package com.cephcoding.features.feat_search


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.cephcoding.core.domain.model.Article
import com.cephcoding.core.domain.repository.PulseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: PulseRepository
) : ViewModel() {

    private val _searchedNews = MutableStateFlow<PagingData<Article>>(PagingData.empty())
    val searchedNews = _searchedNews.asStateFlow()

    fun searchedForNews(query: String) {
        viewModelScope.launch {
            repository.getSearchedNews(query = query).collect {
                _searchedNews.value = it
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