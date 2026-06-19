package com.cephcoding.core.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cephcoding.core.domain.model.Routes
import com.cephcoding.core.ui.theme.iconBackgroundColor
import com.cephcoding.core.ui.theme.primaryText
import com.cephcoding.core.ui.theme.tertiaryCardColor


@Composable
fun PulseBottomBar(
    navController: NavHostController
) {
    val bottomBarList = listOf(
        Routes.Home,
        Routes.Favorite,
        Routes.Activity,
        Routes.Profile
    )
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route


    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(5.dp)
                .height(80.dp)
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .clip(RoundedCornerShape(20.dp))
                .background(
                    color = tertiaryCardColor.copy(alpha = 0.85f)
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomBarList.forEach { bottomBarItem ->
                val isSelected = currentRoute == bottomBarItem.route

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(0.9f)
                        .padding(7.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) iconBackgroundColor else Color.Transparent)
                        .clickable {
                            if (!isSelected) {
                                navController.navigate(bottomBarItem.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        bottomBarItem.iconRes?.let { resId ->
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(resId),
                                contentDescription = bottomBarItem.title,
                                tint = primaryText
                            )
                        }
                        Text(
                            text = bottomBarItem.title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = primaryText
                        )
                    }
                }
            }
        }
    }

}