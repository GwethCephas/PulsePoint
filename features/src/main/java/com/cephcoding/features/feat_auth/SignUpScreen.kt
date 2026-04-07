package com.cephcoding.features.feat_auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cephcoding.core.authentication.GoogleAuthClient
import com.cephcoding.core.components.CustomTextField
import com.cephcoding.core.domain.model.Routes
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    googleAuthClient: GoogleAuthClient,
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create account",
            modifier = Modifier.padding(20.dp),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold
        )
        CustomTextField(
            modifier = Modifier
                .focusRequester(focusRequester),
            value = username,
            onValueChange = { username = it },
            label = "Username",
            leadingIcon = {

            },
            trailingIcon = null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            visualTransformation = VisualTransformation.None
        )

        Spacer(modifier = Modifier.padding(8.dp))

        CustomTextField(
            modifier = Modifier
                .focusRequester(focusRequester),
            value = email,
            onValueChange = { email = it },
            label = "Email",
            leadingIcon = {

            },
            trailingIcon = null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            visualTransformation = VisualTransformation.None
        )

        Spacer(modifier = Modifier.padding(8.dp))

        CustomTextField(
            modifier = Modifier
                .focusRequester(focusRequester),
            value = password,
            onValueChange = { password = it },
            label = "Password",
            leadingIcon = {

            },
            trailingIcon = {
                IconToggleButton(
                    checked = isPasswordVisible,
                    onCheckedChange = { isPasswordVisible = !isPasswordVisible }
                ) {

                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.padding(8.dp))

        CustomTextField(
            modifier = Modifier
                .focusRequester(focusRequester),
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Confirm Password",
            leadingIcon = {

            },
            trailingIcon = {
                IconToggleButton(
                    checked = isConfirmPasswordVisible,
                    onCheckedChange = { isConfirmPasswordVisible = !isConfirmPasswordVisible }
                ) {

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
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )


        Spacer(modifier = Modifier.padding(8.dp))

        Button(
            modifier = Modifier.fillMaxWidth(0.6f),
            shape = MaterialTheme.shapes.medium,
            onClick = {
                if (password == confirmPassword && username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                    scope.launch {
                        googleAuthClient.createUserWithEmailAndPassword(
                            email = email,
                            password = password,
                            username = username
                        )
                    }
                }
                if (googleAuthClient.getCurrentUser() != null) {
                    navController.navigate(Routes.Home.route)
                }

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = "Create account",
                style = MaterialTheme.typography.bodyMedium,
            )
        }

    }

}
