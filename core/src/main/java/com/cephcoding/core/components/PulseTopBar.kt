package com.cephcoding.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ceph.core.R
import com.cephcoding.core.domain.model.Routes
import com.cephcoding.core.ui.theme.primaryContainerDarkMediumContrast
import com.cephcoding.core.ui.theme.primaryContainerLightMediumContrast
import com.cephcoding.core.ui.theme.surfaceBrightLight


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PulseTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController
) {

    TopAppBar(
        modifier = Modifier
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.inversePrimary,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.inversePrimary
                    )
                )
            ),
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
                            color = MaterialTheme.colorScheme.secondaryContainer,
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
                   modifier = Modifier.size(24.dp),
                   painter = painterResource(R.drawable.search),
                   contentDescription = "Search",
                   tint = Color.White
               )
            }
        },

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )

}