package com.ceph.pulsepoint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ceph.pulsepoint.navigation.NavGraphSetUp
import com.cephcoding.core.authentication.GoogleAuthClient
import com.cephcoding.core.components.PulseBottomBar
import com.cephcoding.core.components.PulseTopBar
import com.cephcoding.core.domain.model.Routes
import com.cephcoding.core.ui.theme.ThePulsePointTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class MainActivity : ComponentActivity() {

    private val googleAuthClient: GoogleAuthClient by inject()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ThePulsePointTheme{

                val scrollState = rememberLazyListState()

                // Navigation setup.
                val navController = rememberNavController()
                val currentRoute =
                    navController.currentBackStackEntryAsState().value?.destination?.route

                var searchQuery by remember { mutableStateOf("") }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.navigationBars),
                    topBar = {
                        if (currentRoute == Routes.Home.route) {
                            PulseTopBar(
                                navController = navController
                            )
                        }

                    },
                    bottomBar = {

                        if (currentRoute != Routes.SignIn.route && currentRoute != Routes.SignUp.route) {
                            PulseBottomBar(
                                navController = navController
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