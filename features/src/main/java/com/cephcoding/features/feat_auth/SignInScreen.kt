package com.cephcoding.features.feat_auth


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
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
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primaryContainer
                    )
                )
            ),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        CustomButton(
            text = "Sign in with Google",
            onClick = {
                scope.launch {
                    googleAuthClient.signInWithCredentialManager()
                    if (googleAuthClient.getCurrentUser() != null) {
                        navController.navigate(Routes.Home.route)
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary
        )
        CustomButton(
            text = "Sign in with Email",
            onClick = {
                isSignInBottomBarVisible = true
            },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )

        val annotatedString = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.scrim
                )
            ) {
                append("Don't have an account? ")
            }
            val signUpLink = LinkAnnotation.Clickable(
                tag = "Sign Up",
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = Color.Blue,
                        fontWeight = FontWeight.Bold
                    )

                ),
                linkInteractionListener = {
                    navController.navigate(Routes.SignUp.route)
                }
            )
            withLink(signUpLink) {
                append(" Sign Up")
            }
        }

        Text(
            modifier = Modifier.padding(10.dp),
            text = annotatedString,
            style = MaterialTheme.typography.bodyMedium,
        )
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

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(0.8f)
            .height(65.dp)
            .padding(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Text(
            modifier = modifier,
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }

}