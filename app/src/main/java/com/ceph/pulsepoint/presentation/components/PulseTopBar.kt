package com.ceph.pulsepoint.presentation.components


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ceph.pulsepoint.presentation.navigation.Routes
import com.ceph.pulsepoint.ui.theme.primaryContainerDarkMediumContrast
import com.ceph.pulsepoint.ui.theme.primaryContainerLightMediumContrast
import com.ceph.pulsepoint.ui.theme.surfaceBrightLight


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PulseTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController
) {

    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = surfaceBrightLight,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append("Pulse")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = primaryContainerDarkMediumContrast,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append("Point")
                    }

                }
            )
        },
        actions = {
            IconButton(
                onClick = {
                    navController.navigate(Routes.Search.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search"
                )
            }
        },

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = primaryContainerLightMediumContrast
        )
    )

}