package edu.temple.convoy.login_flow.screen

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
import edu.temple.convoy.utils.Constant
import edu.temple.convoy.ui.components.CustomButton
import edu.temple.convoy.ui.components.CustomClickableText
import edu.temple.convoy.ui.components.CustomDivider
import edu.temple.convoy.ui.components.CustomOutlinedPasswordField
import edu.temple.convoy.ui.components.CustomOutlinedTextField
import edu.temple.convoy.ui.components.CustomText
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
        CustomText(
            text = stringResource(id = R.string.greeting),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        CustomText(
            text = stringResource(id = R.string.sign_in_message),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp
        )

        CustomOutlinedTextField(
            value = username,
            label = stringResource(id = R.string.user_name),
            icon = R.drawable.user,
            onValueChange = {
                username = it
            }
        )

        CustomOutlinedPasswordField(
            value = password,
            label = stringResource(id = R.string.password),
            icon = R.drawable.password,
            onValueChange = {
                password = it
            }
        )

        Spacer(modifier = Modifier.height(100.dp))

        CustomButton(
            text = stringResource(id = R.string.sign_in),
            onClick = {
                coroutineScope.launch {
                    val response = RetrofitClient.instance.loginUser(
                        action = Constant.LOGIN,
                        username = username,
                        password = password
                    )

                    if (response.status == Constant.SUCCESS) {
                        response.sessionKey?.run {
                            with(sharedPreferences.edit()) {
                                putString(Constant.SESSION_KEY, this@run)
                                putString(Constant.USERNAME, username)
                                apply()
                            }
                            showToast(
                                context,
                                "Signed in successfully"
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

        CustomDivider()

        CustomClickableText(
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