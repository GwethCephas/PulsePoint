package com.cephcoding.core.domain.model

import com.ceph.core.R

sealed class Routes(
    val title: String,
    val route: String,
    val iconRes: Int? = null,
) {

    data object Home : Routes(
        title = "Home",
        route = "home",
        iconRes = R.drawable.home
    )

    data object Profile : Routes(
        title = "Profile",
        route = "profile",
        iconRes = R.drawable.profile
    )

    data object Favorite : Routes(
        title = "Favorite",
        route = "favorite",
        iconRes = R.drawable.favorite
    )
    data object Activity: Routes(
        title = "Activity",
        route = "activity",
        iconRes = R.drawable.notification

    )
    data object Search :Routes(
        title = "Search",
        route = "search"
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