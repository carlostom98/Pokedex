package com.example.dailyjobs.View.Screens

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dailyjobs.Model.Pokemon2Model.PokedexProperties
import com.example.dailyjobs.Model.PokemonListModel.PokemonListEntry
import com.example.dailyjobs.R
import com.example.dailyjobs.View.DestinationScreen
import com.example.dailyjobs.ViewModel.PokemonViewModel.PokemonViewModel
import com.example.dailyjobs.ViewModel.PokemonViewModel.ViewModelsFactory


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
            PokemonEntry(
                pokedexModel = PokemonListEntry(
                    "Primero",
                    "https://media.printables.com/media/prints/30233/images/300606_05d12d0c-053b-47c7-a29d-57ec89e338e8/thumbs/inside/1280x960/png/bulbasaur_original.webp",
                    1
                ), navHost = navHost
            )
        }
    }
}


@Composable
fun CardView(listOfModel: PokedexProperties) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Row() {
                Column(modifier = Modifier.weight(1f)) {

                }

                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Show Me More")
                }
            }
        }
    }
}

@Composable
fun RecyclerView(listOfPokemons: List<PokedexProperties>) {
    LazyColumn(modifier = Modifier.padding(4.dp)) {
        items(listOfPokemons) {
            CardView(listOfModel = it)
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
    modifier: Modifier = Modifier,
) {
    val pokemonViewModel = viewModel<PokemonViewModel>(factory = ViewModelsFactory)
    val defaultDominantColor = MaterialTheme.colorScheme.surface
    var dominantColor by remember { mutableStateOf(defaultDominantColor) }

    Box(
        contentAlignment = Center,
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
                    pokemonViewModel.calculateDominantColor(it.result.drawable) { newColor ->
                        dominantColor = newColor
                    }
                },
                modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally)
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
            if (entries.size >= pokedexIndex*2 +2){
                PokemonEntry(
                    pokedexModel = entries[pokedexIndex * 2 + 1],
                    navHost = navHost,
                    modifier = Modifier.weight(1f)
                )
            }else{
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}



