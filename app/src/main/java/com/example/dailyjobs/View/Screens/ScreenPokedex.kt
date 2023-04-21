package com.example.dailyjobs.View.Screens

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.internal.updateLiveLiteralValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.dailyjobs.ViewModel.PokemonViewModel.PokemonViewModel
import com.example.dailyjobs.ViewModel.PokemonViewModel.ViewModelsFactory
import kotlinx.coroutines.delay

@Composable
fun ScreenPokedex(navHost:NavHostController){

    var animationState by remember { mutableStateOf(true) }
    val transition = updateTransition(targetState = animationState, label = null)

    val rotation by transition.animateFloat(transitionSpec = {tween(500)}, label = "") {
        if(it) 0f else 360f
    }
    val color by transition.animateColor(transitionSpec = {tween(500)}, label = "") {
        if(it) Color.Transparent else Color.Yellow
    }

    val pokemonViewModel= viewModel<PokemonViewModel>(factory = ViewModelsFactory)
    val pokemon by pokemonViewModel.pokemonInfo.observeAsState()
    var pokemonName by remember { mutableStateOf("") }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 8.dp, horizontal = 8.dp)){
        Column(modifier= Modifier
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .fillMaxWidth()) {
            Row(modifier=Modifier.fillMaxWidth()) {
                Text(text = "# Of Pokemon    Name Of Pokemon  $pokemonName  ")
            }
            IconButton(onClick = {animationState=!animationState
            pokemonViewModel.getPokemonInfo(1)
            }) {
                Icon(imageVector = Icons.Default.Favorite, contentDescription ="Favorite Button", tint= color, modifier = Modifier.rotate(rotation))
            }
        }
    }

    LaunchedEffect(pokemon){
        pokemon?.let {
            pokemonName=it.name
        }
    }
}