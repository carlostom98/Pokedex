package com.elaniin.pokeapptest.View.Screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elaniin.pokeapptest.FirebaseDataBase.DataBasePokemon
import com.elaniin.pokeapptest.View.PokedexScrolleable
import com.elaniin.pokeapptest.ViewModel.PokemonViewModel.DataBaseManagerViewModel
import org.koin.androidx.compose.get
import kotlin.math.log

@Composable
fun PokemonInformation(userId:String) {

    val databaseManager: DataBaseManagerViewModel = get()
    databaseManager.getAllPokemonsInDB(userId)
    var listPaginated by remember {
        mutableStateOf(listOf(DataBasePokemon("", null)))
    }
    var globalIndex by remember {
        mutableStateOf(-1)
    }
    val pokemonsList by databaseManager.pokemonFromDB.observeAsState()
    Surface(color = MaterialTheme.colorScheme.primary) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column() {
                pokemonsList?.let {
                    LazyRow() {
                        listPaginated = it
                        Log.d("Pokemons", "$listPaginated")
                        items(it.size) { index ->
                            Button(onClick = { globalIndex = index }) {
                                Text(text = "${index + 1}")
                            }
                        }
                    }
                }
                if (globalIndex > -1) {
                    Text(
                        text = listPaginated[globalIndex].groupName,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        style = TextStyle(color = Color.Yellow, fontSize = 30.sp, fontStyle = FontStyle.Italic)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    LazyColumn(contentPadding = PaddingValues(16.dp)) {
                        val itemCout = if (listPaginated[globalIndex].pokemonValues!!.size % 2 == 0) {
                            listPaginated[globalIndex].pokemonValues!!.size / 2
                        } else {
                            listPaginated[globalIndex].pokemonValues!!.size / 2 + 1
                        }
                        items(itemCout) { index ->
                            PokedexScrolleable(pokedexIndex = index, entries = listPaginated[globalIndex].pokemonValues!!)
                        }
                    }
                }
            }
        }
    }
    LaunchedEffect(key1 = pokemonsList) {
        if (pokemonsList != null) {

        }
    }
}