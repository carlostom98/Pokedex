package com.example.dailyjobs.View

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dailyjobs.Model.PokemonListModel.PokemonListEntry
import com.example.dailyjobs.ViewModel.PokemonViewModel.ColorBackGroundViewModel
import com.example.dailyjobs.ViewModel.PokemonViewModel.PokemonViewModel
import org.koin.androidx.compose.get


@Composable
fun PokemonRecyclerView(navHost: NavHostController?, pokemonViewModel: PokemonViewModel = get()) {
  /*  val isSuccesObserver by pokemonViewModel.is_succes.observeAsState(false)
    val isErrorObserver by pokemonViewModel.is_error.observeAsState("")
    val isLoadingObserver by pokemonViewModel.is_loading.observeAsState(false)*/
    val pokeList by pokemonViewModel.pokemonList.observeAsState(emptyList())
    LazyColumn(contentPadding = PaddingValues(16.dp) ){
        val itemCout=if(pokeList.size %2 ==0){
            pokeList.size/2
        }else{
            pokeList.size/2 + 1
        }
        items(itemCout){
            if(it>=itemCout -1){
                pokemonViewModel.loadPaginatingPokemon()
            }
            PokedexScrolleable(pokedexIndex = it, entries =pokeList , navHost =navHost!! )
        }
    }
}


@Composable
fun SearchBar(modifier: Modifier = Modifier, hint: String = "", onSearch: (String) -> Unit = {}) {
    var text by remember { mutableStateOf("") }
    var isHintDisplayed by remember { mutableStateOf(hint != "") }

    Box(modifier = modifier) {
        BasicTextField(
            value = text, onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged { isHintDisplayed = !it.isFocused }
        )
        if (isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun PokemonEntry(
    pokedexModel: PokemonListEntry,
    navHost: NavHostController?,
    colorBackGViewModel: ColorBackGroundViewModel = get(),
    modifier: Modifier = Modifier,
) {

    val defaultDominantColor = MaterialTheme.colorScheme.surface
    var dominantColor by remember { mutableStateOf(defaultDominantColor) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(100.dp, RoundedCornerShape(50.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(Brush.verticalGradient(listOf(dominantColor, defaultDominantColor)))
            .clickable {
                navHost?.navigate(
                    DestinationScreen.PokemonDetailScreen.withPokemonName(
                        pokedexModel.name,
                        null
                    )
                )
            }
    ) {
        Column {
            AsyncImage(
                ImageRequest.Builder(LocalContext.current)
                    .data(pokedexModel.image)
                    .crossfade(true)
                    .build(),
                contentDescription = pokedexModel.name,
                onSuccess = {
                    colorBackGViewModel.calculateDominantColor(it.result.drawable)
                },
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = pokedexModel.name,
                style = TextStyle(Color.Black, fontSize = 20.sp, fontStyle = FontStyle.Italic),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    //TODO:ViewModel Color UPDATED WITH LIVEDATA
    val newColor by colorBackGViewModel.newColor.observeAsState(defaultDominantColor)
    LaunchedEffect(key1 = newColor) {
        dominantColor = newColor
    }
}

@Composable
fun PokedexScrolleable(
    pokedexIndex: Int,
    entries: List<PokemonListEntry>,
    navHost: NavHostController,
) {
    Column() {
        Row() {
            PokemonEntry(
                pokedexModel = entries[pokedexIndex * 2],
                navHost = navHost,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(15.dp))
            if (entries.size >= pokedexIndex * 2 + 2) {
                PokemonEntry(
                    pokedexModel = entries[pokedexIndex * 2 + 1],
                    navHost = navHost,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

