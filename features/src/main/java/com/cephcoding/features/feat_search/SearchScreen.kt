package com.cephcoding.features.feat_search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.ceph.features.R
import com.cephcoding.core.components.ArticleItem
import com.cephcoding.core.domain.model.Article
import com.cephcoding.core.ui.theme.backgroundColor
import com.cephcoding.core.ui.theme.primaryText
import com.cephcoding.core.ui.theme.secondaryText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    searchedNews: LazyPagingItems<Article>,
    onSearch: (String) -> Unit,
    listState: LazyListState,
    favoriteArticlesUrls: List<String>,
    onToggleStatus: (Article) -> Unit
) {

    val keyBoardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(5.dp)
    ) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            colors = SearchBarDefaults.colors(
                containerColor = secondaryText
            ),
            tonalElevation = SearchBarDefaults.TonalElevation,
            shadowElevation = SearchBarDefaults.ShadowElevation,
            query = searchQuery,
            onQueryChange = onQueryChange,
            onSearch = {
                onSearch(searchQuery)
                keyBoardController?.hide()

            },
            leadingIcon = {

            },
            trailingIcon = {
                IconButton(onClick = { if (searchQuery.isNotEmpty()) onQueryChange("") }) {

                }
            },
            active = false,
            onActiveChange = {},
            placeholder = {
                Text(text = "Search here")
            },
            content = {},

            )

        if (searchedNews.itemCount == 0) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_data),
                    contentDescription = "empty",
                    tint = secondaryText
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Search for news",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = primaryText
                )

            }

        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(searchedNews.itemSnapshotList.size) { searchArticle ->
                    searchedNews[searchArticle]?.let { article ->
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