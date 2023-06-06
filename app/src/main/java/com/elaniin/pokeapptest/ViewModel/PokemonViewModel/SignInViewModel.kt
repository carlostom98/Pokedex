package com.elaniin.pokeapptest.ViewModel.PokemonViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elaniin.pokeapptest.Athentication.GoogleAuthentication
import com.elaniin.pokeapptest.Athentication.SignInState
import com.elaniin.pokeapptest.Athentication.SignOutResult
import com.elaniin.pokeapptest.Resource
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SignInViewModel(private val googleAuthentication:GoogleAuthentication): ViewModel() {
    private val _googleState=MutableLiveData(SignInState())
    private val _googleSignOutState=MutableLiveData(SignOutResult())
    val googleState:LiveData<SignInState> get() = _googleState
    val googleSignOutState:LiveData<SignOutResult> get() = _googleSignOutState

    private val _currentUser=MutableLiveData<String>()
    val currentUser:LiveData<String> get()= _currentUser



    fun googleSignIn(credential: AuthCredential)=viewModelScope.launch {
        googleAuthentication.googleSignIn(credential).collect(){result->
            when(result){
                is Resource.Succes -> {
                    _googleState.postValue(SignInState(isSignProcessSucces = result.data!!.result))
                    _googleSignOutState.postValue(SignOutResult(isSignOutProcessSucces = false))
                    _googleSignOutState.postValue(SignOutResult(isLoading = false))
                    _currentUser.postValue(result.data.userId)
                }
                is Resource.Loading -> {
                    _googleState.postValue(SignInState(isLoading = true))
                }
                is Resource.Error -> {
                    _googleState.postValue(SignInState(signInErrorMessage = result.message))
                }
            }
        }
    }
    fun googleSignOut()=viewModelScope.launch {
        googleAuthentication.googleSignOut().collect(){result->
            when(result){
                is Resource.Succes->{
                    _googleSignOutState.postValue(SignOutResult(isSignOutProcessSucces = true))
                    _googleState.postValue(SignInState(isSignProcessSucces = null))
                    _googleState.postValue(SignInState(isLoading = false))
                }
                is Resource.Error -> {
                    _googleSignOutState.postValue(SignOutResult(signInErrorMessage = result.message))
                }
                is Resource.Loading -> {
                    _googleSignOutState.postValue(SignOutResult(isLoading = true))
                }
            }
        }
    }
}