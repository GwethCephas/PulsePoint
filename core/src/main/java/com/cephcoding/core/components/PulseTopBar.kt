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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ceph.core.R
import com.cephcoding.core.domain.model.Routes
import com.cephcoding.core.ui.theme.backgroundColor
import com.cephcoding.core.ui.theme.iconBackgroundColor
import com.cephcoding.core.ui.theme.primaryText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PulseTopBar(
    navController: NavHostController
) {

    TopAppBar(
        modifier = Modifier
            .background(
                color = backgroundColor
            ),
        title = {

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = primaryText,
                            fontSize = MaterialTheme.typography.displaySmall.fontSize,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append("Pulse")
                    }
                    append(" ")
                    withStyle(
                        style = SpanStyle(
                            color = iconBackgroundColor,
                            fontSize = MaterialTheme.typography.displaySmall.fontSize,
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
                   tint = primaryText
               )
            }
        },

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )

}