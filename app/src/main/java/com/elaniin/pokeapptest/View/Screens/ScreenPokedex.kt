package com.elaniin.pokeapptest.View.Screens


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.BottomEnd

import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.elaniin.pokeapptest.FirebaseDataBase.DataBasePokemon
import com.elaniin.pokeapptest.Tools.Tools
import com.elaniin.pokeapptest.View.DestinationScreen
import com.elaniin.pokeapptest.View.PaginateButtons
import com.elaniin.pokeapptest.View.PokemonRecyclerView
import com.elaniin.pokeapptest.View.SearchBar
import com.elaniin.pokeapptest.ViewModel.PokemonViewModel.DataBaseManagerViewModel
import com.elaniin.pokeapptest.ViewModel.PokemonViewModel.PokemonsSelectedViewModel
import com.elaniin.pokeapptest.ViewModel.PokemonViewModel.SignInViewModel
import org.koin.androidx.compose.get

@Composable
fun ScreenPokedex(navHost: NavHostController?) {
    val context = LocalContext.current
    var textGroup by remember {
        mutableStateOf("")
    }
    val signInViewModel: SignInViewModel = get()
    val pokemonsSelected: PokemonsSelectedViewModel = get()
    val databaseManager: DataBaseManagerViewModel = get()
    val isReached by pokemonsSelected.quantityAchieve.observeAsState(false)

    val signOutState by signInViewModel.googleSignOutState.observeAsState()
    Surface(color = MaterialTheme.colorScheme.primary) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                Spacer(modifier = Modifier.height(20.dp))
                Image(
                    painterResource(id = com.elaniin.pokeapptest.R.drawable.pokemon_logo),
                    contentDescription = "Pokemon Logo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(CenterHorizontally),
                    contentScale = ContentScale.FillWidth,
                    colorFilter = ColorFilter.tint(Color.Yellow)
                )
                Spacer(modifier = Modifier.height(20.dp))
                SearchBar(
                    hint = "Nombre Grupo Pokemon", modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    textGroup = it
                }
                if (isReached) {
                    ButtonConfirmAndViewGroups(text = "Crear Grupo") {
                        if (textGroup.isNotEmpty()) {
                            databaseManager.saveData(textGroup)
                        }
                    }
                }
                ButtonConfirmAndViewGroups(text = "Ver Mis Grupos") {
                    navHost?.navigate(DestinationScreen.PokemonDetailScreen.withPokemonName("SecondScreen", Color.Yellow))
                }
                PaginateButtons()
                PokemonRecyclerView(navHost)
            }
            Button(
                onClick = { signInViewModel.googleSignOut() }, modifier = Modifier
                    .clip(CircleShape)
                    .size(100.dp)
                    .align(BottomEnd),
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Text(
                    text = "Cerrar sesión",
                    style = TextStyle(
                        fontFamily = FontFamily.Cursive,
                        fontStyle = FontStyle.Italic,
                        color = Color.Black,
                        fontSize = 20.sp
                    )
                )
            }
        }
    }

    LaunchedEffect(key1 = signOutState) {
        if (signOutState!!.isSignOutProcessSucces) {
            Toast.makeText(context, "Sign Out Succes", Toast.LENGTH_SHORT).show()
            navHost?.navigate(DestinationScreen.RegisterScreen.baseRoute)
        }
    }
}

@Composable
fun ButtonConfirmAndViewGroups(text: String, onClick: () -> Unit) {
    Box(modifier = Modifier.padding(10.dp)) {
        Button(
            onClick = {
                onClick()
            },
            Modifier
                .clip(RoundedCornerShape(10.dp))
                .height(50.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color.Yellow)
        ) {
            Text(text = text, textAlign = TextAlign.Center, color = Color.Black)
        }
    }
}


