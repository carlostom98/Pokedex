package com.example.pokemonapp.Athentication

import com.google.firebase.auth.AuthResult

data class SignInState(val isSignProcessSucces:AuthResult?=null, val signInErrorMessage:String?=null, val isLoading:Boolean=false)