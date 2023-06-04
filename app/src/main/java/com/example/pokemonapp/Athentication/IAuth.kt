package com.example.pokemonapp.Athentication

import com.example.pokemonapp.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface IAuth {
    fun googleSignIn(credential:AuthCredential): Flow<Resource<AuthResult>>
}