package com.ceph.pulsepoint.presentation.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Routes(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector?,
    val unselectedIcon: ImageVector?
) {

    data object Home : Routes(
        title = "Home",
        route = "home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Filled.Home
    )

    data object Profile : Routes(
        title = "Profile",
        route = "profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Filled.Person
    )

    data object Favorite : Routes(
        title = "Favorite",
        route = "favorite",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder
    )
    data object Search :Routes(
        title = "Search",
        route = "search",
        selectedIcon = Icons.Rounded.Search,
        unselectedIcon = Icons.Outlined.Search
    )
    data object Notification: Routes(
        title = "Notification",
        route = "notification",
        selectedIcon =Icons.Default.Notifications ,
        unselectedIcon =Icons.Outlined.Notifications
    )

    data object SignIn:Routes(
        title = "SignIn",
        route = "signIn",
        selectedIcon = null,
        unselectedIcon = null
    )
    data object SignUp:Routes(
        title = "SignUp",
        route = "signUp",
        selectedIcon = null,
        unselectedIcon = null
    )

}