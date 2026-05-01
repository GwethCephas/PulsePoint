package com.cephcoding.features.feat_home

import android.os.Build
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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class HomeViewModel(
    private val repository: PulseRepository
) : ViewModel() {

    private val _articles = MutableStateFlow<PagingData<Article>>(PagingData.empty())
    val articles = _articles.asStateFlow()

    private val _categoryNews = MutableStateFlow<PagingData<Article>>(PagingData.empty())
    val categoryNews = _categoryNews.asStateFlow()

    private val _breakingNews = MutableStateFlow<List<Article>>(emptyList())
    val breakingNews = _breakingNews.asStateFlow()

    init {
        getNewsHeadlines()
        getBreakingNews()
    }

    fun getNewsHeadlines() {
        viewModelScope.launch {
            repository.getNewsHeadlines().collect {
                _articles.value = it
            }

        }
    }

    fun getBreakingNews() {
        viewModelScope.launch {

            val yesterday = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate.now().minusDays(1).toString()
            } else {
                val cal = Calendar.getInstance()
                cal.add(Calendar.DATE, -1)
                SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.time)
            }


            repository.getTodayNews(
                from = yesterday,
                to = yesterday
            ).collect { breakingNews ->
                _breakingNews.value = breakingNews
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