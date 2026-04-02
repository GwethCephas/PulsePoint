package com.ceph.pulsepoint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.ceph.pulsepoint.data.worker.RandomNewsWorker
import com.ceph.pulsepoint.presentation.authentication.GoogleAuthClient
import com.ceph.pulsepoint.presentation.components.PulseBottomBar
import com.ceph.pulsepoint.presentation.components.PulseTopBar
import com.ceph.pulsepoint.presentation.navigation.NavGraphSetUp
import com.ceph.pulsepoint.presentation.navigation.Routes
import com.ceph.pulsepoint.ui.theme.ThePulsePointTheme
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {

    private val googleAuthClient by lazy {
        GoogleAuthClient(context = this)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ThePulsePointTheme {

                // Work Manager setup.
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

                val newsPeriodicWorkRequest = PeriodicWorkRequestBuilder<RandomNewsWorker>(
                    1, TimeUnit.HOURS
                )
                    .setConstraints(constraints)
                    .setInitialDelay(10, TimeUnit.SECONDS)
                    .build()

                WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                    "news_periodic_work",
                    ExistingPeriodicWorkPolicy.KEEP,
                    newsPeriodicWorkRequest
                )

                //  Controlling the bottom bar visibility.
                val scrollState = rememberLazyListState()
                var isBottomBarVisible by remember { mutableStateOf(true) }
                var lastScrollIndex by remember { mutableIntStateOf(0) }

                LaunchedEffect(key1 = remember { derivedStateOf { scrollState.firstVisibleItemIndex } }) {
                    if (scrollState.firstVisibleItemIndex < lastScrollIndex) {
                        isBottomBarVisible = true
                    } else if (scrollState.firstVisibleItemIndex > lastScrollIndex) {
                        isBottomBarVisible = false
                    }
                    lastScrollIndex = scrollState.firstVisibleItemIndex
                }

                // Navigation setup.
                val navController = rememberNavController()
                val currentRoute =
                    navController.currentBackStackEntryAsState().value?.destination?.route

                //TopBar scroll behaviour.
                val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

                var searchQuery by remember { mutableStateOf("") }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
                    topBar = {
                        if (currentRoute == Routes.Home.route) {
                            PulseTopBar(
                                scrollBehavior = topAppBarScrollBehavior,
                                navController = navController
                            )
                        }

                    },
                    bottomBar = {

                        if (currentRoute != Routes.SignIn.route && currentRoute != Routes.SignUp.route) {
                            PulseBottomBar(
                                navController = navController,
                                isVisible = isBottomBarVisible
                            )
                        }
                    }
                ) { paddingValues ->

                    NavGraphSetUp(
                        navController = navController,
                        listState = scrollState,
                        searchQuery = searchQuery,
                        onQueryChange = {
                            searchQuery = it
                        },
                        paddingValues = paddingValues,
                        googleAuthClient = googleAuthClient,
                        onSignOut = {
                            lifecycleScope.launch {
                                googleAuthClient.signOut()
                                navController.navigate(Routes.SignIn.route)
                            }
                        }
                    )
                }


            }
        }
    }


}