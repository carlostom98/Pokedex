package com.elaniin.pokeapptest.Athentication

import com.elaniin.pokeapptest.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface IAuth {
    fun googleSignIn(credential:AuthCredential): Flow<Resource<AuthResult>>
    fun googleSignOut():Flow<Resource<Boolean>>
}