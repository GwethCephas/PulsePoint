package com.cephcoding.core.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

sealed class Routes(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector? = null,
    val unselectedIcon: ImageVector? = null
) {

    data object Home : Routes(
        title = "Home",
        route = "home"
    )

    data object Profile : Routes(
        title = "Profile",
        route = "profile"
    )

    data object Favorite : Routes(
        title = "Favorite",
        route = "favorite"
    )
    data object Search :Routes(
        title = "Search",
        route = "search"
    )
    data object Notification: Routes(
        title = "Notification",
        route = "notification"
    )

    data object SignIn:Routes(
        title = "SignIn",
        route = "signIn"
    )
    data object SignUp:Routes(
        title = "SignUp",
        route = "signUp"
    )

}