package com.example.pokemonapp.Athentication

import com.example.pokemonapp.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class GoogleAuthentication():IAuth{
    private val firebaseAuth = FirebaseAuth.getInstance()
    override fun googleSignIn(credential: AuthCredential): Flow<Resource<AuthResult>> {
        return flow{
            emit(Resource.Loading())
            val result = firebaseAuth.signInWithCredential(credential).await()
            emit(Resource.Succes(result))
        }.catch {
            emit(Resource.Error(null, it.message.toString()))
        }
    }
}