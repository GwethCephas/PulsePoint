package com.cephcoding.features.feat_auth


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cephcoding.core.authentication.GoogleAuthClient
import com.cephcoding.core.components.SignInBottomSheet
import com.cephcoding.core.domain.model.Routes
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    googleAuthClient: GoogleAuthClient,
    navController: NavHostController
) {

    var isSignInBottomBarVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = googleAuthClient.getCurrentUser()) {
        if (googleAuthClient.getCurrentUser() != null) {
            navController.navigate(Routes.Home.route)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Button(
            onClick = {
                scope.launch {
                    googleAuthClient.signInWithCredentialManager()
                    if (googleAuthClient.getCurrentUser() != null) {
                        navController.navigate(Routes.Home.route)
                    }
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 15.dp)
        ) {
            Text(
                text = "Sign in with Google",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                isSignInBottomBarVisible = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 15.dp)
        ) {
            Text(
                text = "Sign in with Email",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Don't have an account?")
            TextButton(
                onClick = {
                    navController.navigate(Routes.SignUp.route)
                }
            ) {
                Text(
                    text = "Sign up",
                    color = Color.Blue
                )
            }
        }


    }
    if (isSignInBottomBarVisible) {
        SignInBottomSheet(
            googleAuthClient = googleAuthClient,
            onDismissRequest = {
                isSignInBottomBarVisible = false
            },
            navController = navController
        )
    }


}