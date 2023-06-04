package com.example.pokemonapp.Model.diModules

import com.example.pokemonapp.Athentication.GoogleAuthentication
import com.example.pokemonapp.ViewModel.PokemonViewModel.ColorBackGroundViewModel
import com.example.pokemonapp.ViewModel.PokemonViewModel.PokemonViewModel
import com.example.pokemonapp.ViewModel.PokemonViewModel.SignInViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moduleVM = module {
    single { PokemonViewModel()}
    single { SignInViewModel(get())}
    single { GoogleAuthentication() }
    viewModel<ColorBackGroundViewModel> ()
}