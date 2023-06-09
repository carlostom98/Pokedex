package com.elaniin.pokeapptest.View.Screens

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import com.elaniin.pokeapptest.Athentication.SignInState
import com.elaniin.pokeapptest.R
import com.elaniin.pokeapptest.Tools.Tools
import com.elaniin.pokeapptest.View.DestinationScreen
import com.elaniin.pokeapptest.ViewModel.PokemonViewModel.SignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@Composable
fun RegisterScreen(navHost: NavHostController) {
    val context = LocalContext.current

    val signInViewModel: SignInViewModel = get()

    val googleSignInState by  signInViewModel.googleState.observeAsState()
    val signUser by  signInViewModel.currentUser.observeAsState()
    var userToShare by remember{ mutableStateOf("") }


    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val result = account.getResult(ApiException::class.java)
                val credentials = GoogleAuthProvider.getCredential(result.idToken, null)
                signInViewModel.googleSignIn(credentials)
            } catch (it: ApiException) {
                Log.d("EXCEPTION_LOGIN", "${it.message}")
            }
        }


    Surface(color = MaterialTheme.colorScheme.primary) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Gifmage(resource = R.drawable.brianshepard_pokeballgo)
            Column(modifier=Modifier.align(Alignment.BottomCenter)) {
                ButtonRegister(text = "Google Register") {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestIdToken(Tools.TOKEN_GOOGLE_CLIENT)
                        .build()
                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    launcher.launch(googleSignInClient.signInIntent)
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    LaunchedEffect(key1 = googleSignInState?.isSignProcessSucces) {
        if(googleSignInState?.isSignProcessSucces !=null && userToShare != ""){
            Toast.makeText(context, "Sign In Succes", Toast.LENGTH_SHORT).show()
            navHost.navigate(DestinationScreen.PokedexScreen.withUserId(userToShare))
        }
    }
    LaunchedEffect(key1 = signUser ){
        signUser?.let {
            userToShare=it
        }
    }
}

@Composable
fun ButtonRegister(text: String, onClick: () -> Unit) {
    Button(
        onClick = { onClick() }, modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(Color.Yellow)
    ) {
        Row() {
            Image(painter = painterResource(id = R.drawable.registrocongoogle) , contentDescription = "Icono google")
            Text(
                text = text, Modifier.background(Color.Transparent), style = TextStyle(
                    color = Color.Black,
                    fontSize = 20.sp
                )
            )
        }
    }
}
@Composable
fun Gifmage(resource:Int){
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            add(ImageDecoderDecoder.Factory())
        }.build()
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = rememberAsyncImagePainter(
                model = resource,
                imageLoader
            ), contentDescription = "ImageMainGif", modifier = Modifier
                .size(400.dp)
                .align(Alignment.Center)
        )
    }
}