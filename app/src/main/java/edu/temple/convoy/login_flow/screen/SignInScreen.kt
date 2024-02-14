package edu.temple.convoy.login_flow.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.preference.PreferenceManager
import edu.temple.convoy.R
import edu.temple.convoy.login_flow.data.RetrofitClient
import edu.temple.convoy.ui.components.CustomizedButton
import edu.temple.convoy.ui.components.CustomizedClickableText
import edu.temple.convoy.ui.components.CustomizedDivider
import edu.temple.convoy.ui.components.CustomizedOutlinedPasswordField
import edu.temple.convoy.ui.components.CustomizedOutlinedTextField
import edu.temple.convoy.ui.components.CustomizedText
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    goToSignUpClick: () -> Unit,
    onSignInSuccess: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomizedText(
            text = stringResource(id = R.string.greeting),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        CustomizedText(
            text = stringResource(id = R.string.sign_in_message),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp
        )

        CustomizedOutlinedTextField(
            value = username,
            label = stringResource(id = R.string.user_name),
            icon = R.drawable.user,
            onValueChange = {
                username = it
            }
        )

        CustomizedOutlinedPasswordField(
            value = password,
            label = stringResource(id = R.string.password),
            icon = R.drawable.password,
            onValueChange = {
                password = it
            }
        )

        Spacer(modifier = Modifier.height(100.dp))

        CustomizedButton(
            text = stringResource(id = R.string.sign_in),
            onClick = {
                coroutineScope.launch {
                    val response = RetrofitClient.instance.loginUser(
                        action = "LOGIN",
                        username = username,
                        password = password
                    )

                    if (response.status == "SUCCESS") {
                        response.sessionKey?.run {
                            with(sharedPreferences.edit()) {
                                putString("session_key", this@run)
                                putString("username", username)
                                apply()
                            }
                            showToast(
                                context,
                                "$this@run"
                            )
                        }

                        onSignInSuccess()
                    } else {
                        showToast(
                            context,
                            "${response.message}"
                        )

                        username = ""
                        password = ""
                    }
                }
            }
        )

        CustomizedDivider()

        CustomizedClickableText(
            text = stringResource(id = R.string.no_account),
            goToText = stringResource(id = R.string.register)
        ) {
            goToSignUpClick()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignIn() {
    SignInScreen({}, {})
}