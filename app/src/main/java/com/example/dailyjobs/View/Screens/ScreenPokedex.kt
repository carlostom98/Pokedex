package com.example.dailyjobs.View.Screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dailyjobs.View.PaginateButtons
import com.example.dailyjobs.View.PokemonRecyclerView
import com.example.dailyjobs.View.SearchBar




@Composable
fun ScreenPokedex(navHost: NavHostController?) {
    Surface(color = MaterialTheme.colorScheme.primary) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painterResource(id = com.example.dailyjobs.R.drawable.pokemon_logo),
                contentDescription = "Pokemon Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally),
                contentScale = ContentScale.FillWidth,
                colorFilter = ColorFilter.tint(Color.Yellow)
            )
            Spacer(modifier = Modifier.height(20.dp))
            SearchBar(
                hint = "Search Pokemon By Name...", modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

            }
            PaginateButtons()
            PokemonRecyclerView(navHost)
        }
    }
}


