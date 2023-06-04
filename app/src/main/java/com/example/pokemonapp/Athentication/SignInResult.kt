package com.example.pokemonapp.Athentication

import android.media.session.MediaSessionManager.RemoteUserInfo


//Contains the user data
data class SignInResult(val data: UserData?, val errorMessage: String?)

data class UserData(val userId: String?, val userName: String?, val profilePictureUrl: String?)