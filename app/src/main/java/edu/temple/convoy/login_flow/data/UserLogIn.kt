package edu.temple.convoy.login_flow.data

data class UserLogIn(
    val action: String,
    val userName: String,
    val firstName: String?,
    val lastName: String?,
    val password: String
)
