package com.elaniin.pokeapptest.Athentication

import com.google.firebase.auth.AuthResult

data class SignOutResult(
    val isSignOutProcessSucces: Boolean = false,
    val signInErrorMessage: String? = null,
    val isLoading: Boolean = false,
)