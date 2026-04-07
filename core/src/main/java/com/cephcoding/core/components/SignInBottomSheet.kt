package com.cephcoding.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ceph.core.R
import com.cephcoding.core.authentication.GoogleAuthClient
import com.cephcoding.core.domain.model.Routes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    googleAuthClient: GoogleAuthClient,
    navController: NavHostController
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isVisible by remember { mutableStateOf(false) }


    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = null,
        modifier = modifier.wrapContentHeight()
    ) {
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "Sign in with Email",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.scrim,
                fontWeight = FontWeight.SemiBold
            )
            CustomTextField(
                modifier = Modifier
                    .focusRequester(emailFocusRequester),
                value = email,
                onValueChange = { email = it },
                label = "Email",
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.email),
                        contentDescription = "Email",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        passwordFocusRequester.requestFocus()
                    }
                ),
                visualTransformation = VisualTransformation.None
            )

            Spacer(modifier = Modifier.padding(8.dp))

            CustomTextField(
                modifier = Modifier
                    .focusRequester(passwordFocusRequester),
                value = password,
                onValueChange = { password = it },
                label = "Password",
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.lock),
                        contentDescription = "Email",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    IconToggleButton(
                        checked = isVisible,
                        onCheckedChange = { isVisible = !isVisible }
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(
                                if (isVisible) R.drawable.visibility_off
                                else R.drawable.visibility_on
                            ),
                            contentDescription = "Password Visibility",
                            tint = MaterialTheme.colorScheme.primary
                        )

                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Button(
                modifier = Modifier.fillMaxWidth(0.5f),
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        scope.launch {
                            googleAuthClient.signInWithEmailAndPassword(email, password)
                        }
                    }
                    onDismissRequest()
                    if (googleAuthClient.getCurrentUser() != null) {
                        navController.navigate(Routes.Home.route)
                    }
                },
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "Sign In",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        }
    }

}