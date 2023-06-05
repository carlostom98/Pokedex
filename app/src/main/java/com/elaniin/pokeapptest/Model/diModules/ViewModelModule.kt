package com.elaniin.pokeapptest.Model.diModules

import com.elaniin.pokeapptest.Athentication.GoogleAuthentication
import com.elaniin.pokeapptest.FirebaseDataBase.DataBaseImplementation
import com.elaniin.pokeapptest.FirebaseDataBase.DataBaseManagerFirebaseFirestore
import com.elaniin.pokeapptest.ViewModel.PokemonViewModel.ColorBackGroundViewModel
import com.elaniin.pokeapptest.ViewModel.PokemonViewModel.DataBaseManagerViewModel
import com.elaniin.pokeapptest.ViewModel.PokemonViewModel.PokemonViewModel
import com.elaniin.pokeapptest.ViewModel.PokemonViewModel.SignInViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moduleVM = module {
    single { PokemonViewModel()}
    single { SignInViewModel(get())}
    single { GoogleAuthentication() }
    single { DataBaseManagerViewModel(get()) }
    single<DataBaseImplementation>{ DataBaseManagerFirebaseFirestore() }
    viewModel<ColorBackGroundViewModel> ()
}