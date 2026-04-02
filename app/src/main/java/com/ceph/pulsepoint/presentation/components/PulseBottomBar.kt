package com.ceph.pulsepoint.presentation.components


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ceph.pulsepoint.presentation.navigation.Routes
import com.ceph.pulsepoint.ui.theme.primaryContainerLightMediumContrast
import com.ceph.pulsepoint.ui.theme.surfaceBrightLight


@Composable
fun PulseBottomBar(
    navController: NavHostController,
    isVisible: Boolean
) {


    val bottomBarList = listOf(
        Routes.Home,
        Routes.Favorite,
        Routes.Notification,
        Routes.Profile
    )
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route


    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically { it } + fadeIn(),
        exit = slideOutVertically { it } + fadeOut()
    ) {


        NavigationBar(
            modifier = Modifier
                .height(115.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 15.dp,
                        topEnd = 15.dp
                    )
                ),
            containerColor = MaterialTheme.colorScheme.surfaceVariant,

            ) {

            bottomBarList.forEachIndexed { _, bottomBarItem ->
                NavigationBarItem(
                    selected = currentRoute == bottomBarItem.route,
                    onClick = {
                        if (currentRoute != bottomBarItem.route) {
                            navController.navigate(bottomBarItem.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        (if (currentRoute == bottomBarItem.route) bottomBarItem.selectedIcon
                        else bottomBarItem.unselectedIcon)?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = "icons",
                                tint = if (currentRoute == bottomBarItem.route) surfaceBrightLight
                                else Color.Black
                            )
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = primaryContainerLightMediumContrast
                    ),
                    label = {
                        Text(
                            text = bottomBarItem.title,
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                )
            }
        }
    }


}