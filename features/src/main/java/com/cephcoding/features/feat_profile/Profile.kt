package com.cephcoding.features.feat_profile


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cephcoding.core.authentication.UserData


@Composable
fun ProfileScreen(
    userData: UserData?,
    onSignOut: () -> Unit,

    ) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        AsyncImage(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            model = userData?.photoUrl,
            contentDescription = "Profile picture"
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Name: ${userData?.userName}")

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Email: ${userData?.email}")

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onSignOut) {

            Text(text = "Sign out")
        }

    }

}