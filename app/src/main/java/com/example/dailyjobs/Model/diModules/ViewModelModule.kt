package com.example.dailyjobs.Model.diModules

import com.example.dailyjobs.ViewModel.PokemonViewModel.PokemonViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val moduleVM = module {
    viewModel<PokemonViewModel> ()
}