package com.elaniin.pokeapptest.Athentication

import android.util.Log
import com.elaniin.pokeapptest.FirebaseDataBase.LoginResult
import com.elaniin.pokeapptest.Resource
import com.elaniin.pokeapptest.Tools.Tools
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class GoogleAuthentication() : IAuth {
    private val firebaseAuth = FirebaseAuth.getInstance()
    override fun googleSignIn(credential: AuthCredential): Flow<Resource<LoginResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.signInWithCredential(credential).await()
            val user=firebaseAuth.currentUser
            if(user != null){
                emit(Resource.Succes(LoginResult(result, user.uid)))
                Log.d("USER_ID", user.uid)
            }
        }.catch {
            emit(Resource.Error(null, it.message.toString()))
        }
    }

    override  fun googleSignOut():Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            firebaseAuth.signOut()
            emit(Resource.Succes(true))
        }.catch {
            emit(Resource.Error(null, it.message.toString()))
        }
    }
}