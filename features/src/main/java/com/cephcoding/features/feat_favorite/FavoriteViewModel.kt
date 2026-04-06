package com.cephcoding.features.feat_favorite


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cephcoding.core.domain.model.Article
import com.cephcoding.core.domain.repository.PulseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val repository: PulseRepository
) : ViewModel() {


    val favoriteArticles: StateFlow<PagingData<Article>> = repository.getFavoriteNews()
        .cachedIn(viewModelScope)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            PagingData.empty()
        )

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