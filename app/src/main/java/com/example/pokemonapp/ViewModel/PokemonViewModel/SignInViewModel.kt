package com.example.pokemonapp.ViewModel.PokemonViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonapp.Athentication.GoogleAuthentication
import com.example.pokemonapp.Athentication.SignInState
import com.example.pokemonapp.Athentication.SignOutResult
import com.example.pokemonapp.Resource
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SignInViewModel(private val googleAuthentication:GoogleAuthentication): ViewModel() {
    private val _googleState=MutableLiveData(SignInState())
    private val _googleSignOutState=MutableLiveData(SignOutResult())
    val googleState:LiveData<SignInState> get() = _googleState
    val googleSignOutState:LiveData<SignOutResult> get() = _googleSignOutState


    fun googleSignIn(credential: AuthCredential)=viewModelScope.launch {
        googleAuthentication.googleSignIn(credential).collect(){result->
            when(result){
                is Resource.Succes -> {
                    _googleState.postValue(SignInState(isSignProcessSucces = result.data))
                    _googleSignOutState.postValue(SignOutResult(isSignOutProcessSucces = false))
                    _googleSignOutState.postValue(SignOutResult(isLoading = false))
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