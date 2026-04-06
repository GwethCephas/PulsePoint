package com.cephcoding.features.feat_home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.cephcoding.core.components.ArticleItem
import com.cephcoding.core.domain.model.Article
import com.cephcoding.core.utils.Constants.ALL
import com.cephcoding.core.utils.Constants.BUSINESS
import com.cephcoding.core.utils.Constants.ENTERTAINMENT
import com.cephcoding.core.utils.Constants.GENERAL
import com.cephcoding.core.utils.Constants.HEALTH
import com.cephcoding.core.utils.Constants.SCIENCE
import com.cephcoding.core.utils.Constants.SPORTS
import com.cephcoding.core.utils.Constants.TECHNOLOGY
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    listState: LazyListState,
    favoriteArticlesUrls: List<String>,
    onToggleStatus: (Article) -> Unit,
    paddingValues: PaddingValues,
    viewModel: HomeViewModel
) {

    val categories = listOf(
        ALL, BUSINESS, ENTERTAINMENT, GENERAL, HEALTH, SCIENCE, SPORTS, TECHNOLOGY
    )
    val articles = viewModel.articles.collectAsLazyPagingItems()
    val selectedCategories = viewModel.categoryNews.collectAsLazyPagingItems()
    val selectedCategoriesLoadState = selectedCategories.loadState.refresh
    val loadState = articles.loadState.refresh
    var selectedIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState { categories.size }
    val scope = rememberCoroutineScope()


    LaunchedEffect(selectedIndex) {
        pagerState.animateScrollToPage(selectedIndex)
    }
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedIndex = pagerState.currentPage
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            items(categories.size) {
                SuggestionChip(
                    onClick = {
                        scope.launch {
                            selectedIndex = it
                            viewModel.getNewsByCategory(categories[it])
                        }

                    },
                    label = {
                        Text(text = categories[it])
                    },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = if (selectedIndex == it) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            }

        }

        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fill,
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                when {
                    selectedCategoriesLoadState is LoadState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    selectedCategoriesLoadState is LoadState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Error Occurred")
                        }
                    }

                    loadState is LoadState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    loadState is LoadState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Error Occurred")
                        }
                    }

                    selectedIndex == 0 -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentPadding = PaddingValues(5.dp),
                            state = listState,
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            items(articles.itemSnapshotList.size) { index ->
                                articles[index]?.let { article ->
                                    ArticleItem(
                                        article = article,
                                        onFavoriteClick = { onToggleStatus(article) },
                                        isFavorite = favoriteArticlesUrls.contains(
                                            article.url
                                        )
                                    )
                                }
                            }

                        }

                    }

                    else -> {
                        if (selectedCategories.itemSnapshotList.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(selectedCategories.itemSnapshotList.size) { index ->
                                    selectedCategories[index]?.let { article ->
                                        ArticleItem(
                                            article = article,
                                            onFavoriteClick = { onToggleStatus(article) },
                                            isFavorite = favoriteArticlesUrls.contains(
                                                article.url
                                            )
                                        )
                                    }

                                }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentPadding = PaddingValues(5.dp),
                                state = listState,
                                verticalArrangement = Arrangement.spacedBy(5.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                items(articles.itemSnapshotList.size) { index ->
                                    articles[index]?.let { article ->
                                        ArticleItem(
                                            article = article,
                                            onFavoriteClick = { onToggleStatus(article) },
                                            isFavorite = favoriteArticlesUrls.contains(
                                                article.url
                                            )
                                        )
                                    }
                                }

                            }
                        }
                    }
                }
            }

        }


    }

}