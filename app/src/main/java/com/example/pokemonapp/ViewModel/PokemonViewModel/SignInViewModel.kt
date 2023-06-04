package com.example.pokemonapp.ViewModel.PokemonViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonapp.Athentication.GoogleAuthentication
import com.example.pokemonapp.Athentication.SignInState
import com.example.pokemonapp.Resource
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SignInViewModel(private val googleAuthentication:GoogleAuthentication): ViewModel() {
    private val _googleState=MutableLiveData(SignInState())
    val googleState:LiveData<SignInState> get() = _googleState

    fun googleSignIn(credential: AuthCredential)=viewModelScope.launch {
        googleAuthentication.googleSignIn(credential).collect(){result->
            when(result){
                is Resource.Succes -> {
                    _googleState.postValue(SignInState(isSignProcessSucces = result.data))
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
}