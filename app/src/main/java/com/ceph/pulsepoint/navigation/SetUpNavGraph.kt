package com.ceph.pulsepoint.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.cephcoding.core.authentication.GoogleAuthClient
import com.cephcoding.core.domain.model.Routes
import com.cephcoding.features.feat_auth.SignInScreen
import com.cephcoding.features.feat_auth.SignUpScreen
import com.cephcoding.features.feat_favorite.FavoriteScreen
import com.cephcoding.features.feat_favorite.FavoriteViewModel
import com.cephcoding.features.feat_home.HomeScreen
import com.cephcoding.features.feat_home.HomeViewModel
import com.cephcoding.features.feat_notifications.NotificationScreen
import com.cephcoding.features.feat_profile.ProfileScreen
import com.cephcoding.features.feat_search.SearchScreen
import com.cephcoding.features.feat_search.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavGraphSetUp(
    navController: NavHostController,
    listState: LazyListState,
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    paddingValues: PaddingValues,
    googleAuthClient: GoogleAuthClient,
    onSignOut: () -> Unit
) {

    val startDestination =
        if (googleAuthClient.getCurrentUser() != null) Routes.Home.route else Routes.SignIn.route


    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.Home.route) {

            val homeViewModel = koinViewModel<HomeViewModel>()
            val favoriteArticlesUrls by homeViewModel.favoriteArticlesUrls.collectAsStateWithLifecycle()

            HomeScreen(
                listState = listState,
                onToggleStatus = { article ->
                    homeViewModel.toggleFavoriteStatus(article)
                },
                favoriteArticlesUrls = favoriteArticlesUrls,
                paddingValues = paddingValues,
                viewModel = homeViewModel

            )
        }
        composable(Routes.Favorite.route) {
            val favoriteViewModel = koinViewModel<FavoriteViewModel>()
            val favoriteNewsArticles = favoriteViewModel.favoriteArticles.collectAsLazyPagingItems()
            val favArticlesUrls by favoriteViewModel.favoriteArticlesUrls.collectAsStateWithLifecycle()

            FavoriteScreen(
                listState = listState,
                favoriteArticles = favoriteNewsArticles,
                favoriteArticlesUrls = favArticlesUrls,
                onToggleStatus = { article ->
                    favoriteViewModel.toggleFavoriteStatus(article)
                }
            )
        }
        composable(Routes.Search.route) {

            val searchViewModel = koinViewModel<SearchViewModel>()
            val searchedNews = searchViewModel.searchedNews.collectAsLazyPagingItems()
            val favArticlesUrls by searchViewModel.favoriteArticlesUrls.collectAsStateWithLifecycle()
            SearchScreen(
                listState = listState,
                searchQuery = searchQuery,
                searchedNews = searchedNews,
                onQueryChange = onQueryChange,
                onSearch = { query ->
                    searchViewModel.searchedForNews(query)
                },
                onToggleStatus = { article ->
                    searchViewModel.toggleFavoriteStatus(article)
                },
                favoriteArticlesUrls = favArticlesUrls

            )
        }
        composable(Routes.Activity.route) {
            NotificationScreen()
        }
        composable(Routes.Profile.route) {
            ProfileScreen(
                userData = googleAuthClient.getCurrentUser(),
                onSignOut = onSignOut

            )
        }
        composable(Routes.SignIn.route) {
            SignInScreen(
                googleAuthClient = googleAuthClient,
                navController = navController
            )
        }
        composable(Routes.SignUp.route) {
            SignUpScreen(
                navController = navController,
                googleAuthClient = googleAuthClient
            )
        }
    }
}