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
import edu.temple.convoy.R
import edu.temple.convoy.login_flow.data.RetrofitClient
import edu.temple.convoy.ui.components.CustomButton
import edu.temple.convoy.ui.components.CustomClickableText
import edu.temple.convoy.ui.components.CustomDivider
import edu.temple.convoy.ui.components.CustomOutlinedPasswordField
import edu.temple.convoy.ui.components.CustomOutlinedTextField
import edu.temple.convoy.ui.components.CustomText
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    goToSignInClick: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

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
            text = stringResource(id = R.string.create_account),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp
        )

        CustomOutlinedTextField(
            value = firstname,
            label = stringResource(id = R.string.first_name),
            icon = R.drawable.profile,
            onValueChange = {
                firstname = it
            }
        )
        CustomOutlinedTextField(
            value = lastname,
            label = stringResource(id = R.string.last_name),
            icon = R.drawable.profile,
            onValueChange = {
                lastname = it
            }
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
            text = stringResource(id = R.string.register),
            onClick = {
                coroutineScope.launch {
                    val response = RetrofitClient.instance.registerUser(
                        action = "REGISTER",
                        username = username,
                        firstname = firstname,
                        lastname = lastname,
                        password = password
                    )

                    if (response.status == "SUCCESS") {
                        showToast(
                            context,
                            "Sign up successfully."
                        )
                        goToSignInClick()
                    } else {
                        showToast(
                            context,
                            "${response.message}"
                        )

                        username = ""
                        firstname = ""
                        lastname = ""
                        password = ""
                    }
                }
            }
        )

        CustomDivider()

        CustomClickableText(
            text = stringResource(id = R.string.have_account),
            goToText = stringResource(id = R.string.sign_in)
        ) {
            goToSignInClick()
        }
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

@Preview (showBackground = true)
@Composable
fun PreviewSignUp() {
    SignUpScreen({})
}