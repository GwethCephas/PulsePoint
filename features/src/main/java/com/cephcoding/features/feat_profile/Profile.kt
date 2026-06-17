package com.cephcoding.features.feat_profile


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ceph.core.R
import com.cephcoding.core.authentication.UserData
import com.cephcoding.core.ui.theme.tertiaryCardColor


@Composable
fun ProfileScreen(
    userData: UserData?,
    onSignOut: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .background(MaterialTheme.colorScheme.background)
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                model = userData?.photoUrl,
                contentDescription = "Profile picture"
            )
            CustomText(
                text = "${userData?.userName}"
            )
            CustomText(
                text = "${userData?.email}"
            )

        }


        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            ProfileComponent(
                title = "Language",
                iconRes = R.drawable.language,
                onClick = {
                }
            )
            ProfileComponent(
                title = "Notifications",
                iconRes = R.drawable.notification,
                onClick = {
                }
            )
            ProfileComponent(
                title = "About",
                iconRes = R.drawable.about,
                onClick = {
                }
            )
            ProfileComponent(
                title = "Help",
                iconRes = R.drawable.help,
                onClick = {
                }
            )
            ProfileComponent(
                title = "Log Out",
                iconRes = R.drawable.log_out,
                onClick = {
                    onSignOut()
                }
            )
        }

    }

}


@Composable
fun ProfileComponent(
    modifier: Modifier = Modifier,
    title: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(15.dp)
            )
            .clip(RoundedCornerShape(15.dp))
            .background(
                color = tertiaryCardColor.copy(alpha = 0.85f)
            )
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier
                    .padding(10.dp)
                    .size(27.dp),
                painter = painterResource(
                    iconRes
                ),
                contentDescription = title
            )

            CustomText(
                text = title
            )
        }


        Icon(
            modifier = Modifier
                .padding(10.dp)
                .size(24.dp),
            painter = painterResource(
                R.drawable.right_arrow
            ),
            contentDescription = title
        )
    }
}

@Composable
fun CustomText(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        fontWeight = FontWeight.SemiBold,
        fontSize = MaterialTheme.typography.bodyLarge.fontSize
    )

}