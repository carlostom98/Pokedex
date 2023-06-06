# Pokedex

Create a new project using JetpackCompose:

[New Compose Project](https://developer.android.com/jetpack/compose/setup?hl=es-419#:~:text=If%20you%20already%20have%20an,location%20as%20you%20normally%20would.)

MAIN ACTIVITY:

Call the Navigation method explained later, setting your main compose screen aplication, we also create a navigationHost to navigate among the different screens:


```kotlin
class MainActivity : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navigationHost= rememberNavController()
            DailyJobsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigateScreens(navigationHost = navigationHost, this)
                }
            }
        }
    }
}
```

*IMPORTANT:* Be care about your JDK version

This repository contains the first version of a PokeApp where you can:
- LogIn.LogOut.
- Navigate among the PokeApi [PokeApiV2](https://pokeapi.co/)
- Create your customized pokemon groups using Firebase Database as a Backend.

## Project Structure

To make the code better and scalable, I've implemented different technologies, pulling away the bussines logic and the UI interface maded with JetpackCompose. As:

- MVVM AndroidDesignPattern
- Koin DI

So, my application structure consist in basically different layers:

|# Layer | Structure |
| ------------ | ------------ |
| 1     | Bussines Logic, contain all the direct instaniation from the repositories etc  |
| 2      | KOIN DI 1, We can inject the dependencies from the repositories into our viewModels with Koin |
| 3      | ViewModels, To update in real time our UI implementing LiveData |
| 4      | UI Maded with Jetpack Compose |

# Business Logic

![image](https://github.com/carlostom98/Pokedex/assets/66192349/94a14a9d-8d5a-4266-90b0-e706a130d5cb)

To keep the project sorted, you could find the folders splited.

Our uses cases here are basically:

- API REST with RETROFIT.
- FIREBASE LOGIN
- FIREBASE DATABASE SERVICES.

Open the Model folder 

First import Koin libraries:

```groovy
implementation "io.insert-koin:koin-android:$koin_android_version"
    implementation "io.insert-koin:koin-android-compat:$koin_android_version"
```
Create Koin Modules:

![image](https://github.com/carlostom98/Pokedex/assets/66192349/16185ed7-5cee-4801-b408-f9a9dc6d1620)

Koin will allow us to manage better and easier the DI, next to import and sync your project with koin
As an example you could see how  implement the koin modules here:

```kotlin
val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(Tools.urlPokemonApi)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    single {
        Services(get())
    }
    single{
        GetPokemon(get())
    }
    single {
        GetPokemonList(get())
    }
    single {
        PokemonsSelectedViewModel()
    }
}
```

We need to create a class MyAplication, where the modules of Koin will be created:

![image](https://github.com/carlostom98/Pokedex/assets/66192349/491abb71-b781-4ec2-9052-14abafb1ab4f)

*KOIN OFFICIAL DOCUMENTATION:** [KOIN](https://insert-koin.io/)

This class extends from :Application()  and will configure and start the koin mopdules which we will use. Then declare it into your Manifest

![image](https://github.com/carlostom98/Pokedex/assets/66192349/487cc9b1-8400-411c-965c-3f46792e412b)

Why koin?... Let's see with the API REST

### API REST

To import:

```groovy
//Retrofit Libraries
    implementation "com.squareup.retrofit2:retrofit:$retrofit_version"
    implementation "com.squareup.retrofit2:converter-gson:$retrofit_version"
```

We will find everything about API REST here:

![image](https://github.com/carlostom98/Pokedex/assets/66192349/6fc600a8-70ee-4de2-83da-db4dc79a07f1)

Where:

We create our API SERVICES INTERFACE:

```kotlin
interface ApiService {
    // Get Pokemon
    @GET("pokemon/{name}")
    suspend fun getPokemon(@Path("name")name:String): PokedexProperties
    @GET("pokemon")
    suspend fun getPokemonList(@Query("limit") limit: Int, @Query("offset") offset:Int): PokemonList
}
```
We only need to get data and we also use two different @:
- Path: Allow the app to extend the link for example, you want to add the /1 or /bullbasaur
- Query: Allow the app to make queries without makes a link extension.


*The Services Module*

```kotlin
class Services(private val retrofitService: ApiService) {
    suspend fun getPokemonInfo(name: String): Resource<PokedexProperties> {
        val response= try {
            retrofitService.getPokemon(name)
        }catch (e:Exception){
            return Resource.Error(null, "No pokemon info available")
        }
        return Resource.Succes(response)
    }
    suspend fun getPokemonList(limit:Int, offset:Int):Resource<PokemonList>{
        val response= try{
            retrofitService.getPokemonList(limit,offset)
        }catch(e:Exception){
            return Resource.Error(null, "No pokemon list achieved")
        }
        return Resource.Succes(response)
    }
}
```
Have a dependencies inyection from the ApiService Interface so we could configurate those two functions. If you see with detail, you might realize that the DI is easir with koin for this reazon:

```kotlin
single {
        Retrofit.Builder()
            .baseUrl(Tools.urlPokemonApi)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    single {
        Services(get())
    }
```

We don't need to make an instance from this object we just need get()

Then we have the *GetPokemon()* and *GetPokemonList* which contains a suspend function which makes reference to each different service.

To create our data model of pokemon properties, I implemented this plugging from kotlin:

![image](https://github.com/carlostom98/Pokedex/assets/66192349/95f203e3-c0c1-4d7c-a15b-6b33ac4360ee)

Where you just have to copy and paste the JSON File about pokemon information.

Also I created a PokemonListEntry to plot an specific data into my application.

It's time for our first viewModel: 

![image](https://github.com/carlostom98/Pokedex/assets/66192349/e4ca71d1-8b74-4588-868c-e8384122a740)

The PokemonViewModel class, off course I've created also a ViewModel module with Koin

```kotlin
val moduleVM = module {
    single { PokemonViewModel()}
    single { SignInViewModel(get())}
    single { GoogleAuthentication() }
    single { DataBaseManagerViewModel(get()) }
    single<DataBaseImplementation>{ DataBaseManagerFirebaseFirestore() }
    viewModel<ColorBackGroundViewModel> ()
}
```

This viewModel basically manage the way we read the data retrieved from our API and how it must be served to the UI, this time was necesary just the getPokeminList method but I created the other method to future implementations:

```kotlin
class PokemonViewModel() : ViewModel(), KoinComponent {
    private val getPokemon: GetPokemon = get()
    private val getPokemonList: GetPokemonList = get()
    private var _pokemonInfo = MutableLiveData<PokedexProperties>()
    val pokemonInfo: LiveData<PokedexProperties> get() = _pokemonInfo

    private var _pokemonList = MutableLiveData<List<PokemonListEntry>>()
    private var _is_succes = MutableLiveData<Boolean>()
    private var _is_loading = MutableLiveData<Boolean>()
    private var _is_error = MutableLiveData<String>()

    val pokemonList: LiveData<List<PokemonListEntry>> get() = _pokemonList
    val is_succes: LiveData<Boolean> get() = _is_succes
    val is_loading: LiveData<Boolean> get() = _is_loading
    val is_error: LiveData<String> get() = _is_error

    init {
        loadPaginatingPokemon(0)
    }

    fun getPokemonInfo(name: String) {
        viewModelScope.launch {
            _is_loading.postValue(true)
            getPokemon.invoke(name).let { pokeInfo ->
                when (pokeInfo) {
                    is Resource.Succes -> {
                        pokeInfo.data?.let { pokedex_information ->
                            _pokemonInfo.postValue(
                                pokedex_information
                            )
                        }
                    }
                    is Resource.Error -> {}
                    is Resource.Loading->{}
                }
            }
        }
    }

    fun loadPaginatingPokemon(pageNumber: Int) {
        viewModelScope.launch {
            getPokemonList.invoke(Tools.PAGE_SIZE, (pageNumber) * Tools.PAGE_SIZE).let { pokeList ->
                when (pokeList) {
                    is Resource.Succes -> {
                        _is_succes.postValue(pageNumber * Tools.PAGE_SIZE >= pokeList.data!!.count)
                        pokeList.data.results.map { listData ->
                            val number = if (listData.url.endsWith("/")) {
                                listData.url.dropLast(1).takeLastWhile { it.isDigit() }
                            } else {
                                listData.url.takeLastWhile { it.isDigit() }
                            }.toInt()
                            val urlImage =
                                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/${number}.png"

                            PokemonListEntry(listData.name, urlImage, number)
                        }.let {
                            _pokemonList.postValue(it)
                            _is_loading.postValue(false)
                        }
                    }
                    is Resource.Error -> {
                        _is_error.postValue(pokeList.message!!)
                        _is_loading.postValue(false)
                    }
                    is Resource.Loading->{}
                }
            }
        }
    }
}
```

*IMPORTANT:* Look that the process is running on a coroutine viewModelScope.launch, and the invoke. is a suspend functiond this is maded to keep our main thread free.

Now the UI, the UI is maded with JetpackCompose technology:

[JetpackCompose Documentation](https://developer.android.com/jetpack/compose?hl=es-419)

To make even better and scalable our project, I've implemented the Navigation:

![image](https://github.com/carlostom98/Pokedex/assets/66192349/910684d3-34f8-44e6-9f7b-6532face7441)

Wher We could find the Three Main Screens of our APP:

- Register Screen: LogIn to our app
- ScreenPokedex: Navigate, select and name your Pokemon Group, also you can go out to your application with *"Cerrar sesión"*
- PokemonInformation: Show the user which groups he have created previosly and their names.

Then:

- ScreenComponents: Where I create a lot of different reusable components in Compose.
- Navigate: Which manage our Screen Navigation and, if we want to share some data between.
- DestinationScreen: Which is a sealed class optimized to call our different screens and make easier the data sharing.

Within **ScreenPokedex** you can see the compose of the Pokemons Screen:  

Basically we inject the viewModel and use the observer pattern to update our LazyColumn depend of the page where we are. The method getPokemonList, split the data in ranges of 20 pokemons and set the pokemons in our UI, once we download the data from the API.

**IMPORTANT DETAIL**: To handle our data better, I created the Resource class, allow the aplication manage when the Repository or business logic failed, is loading or is succes.

The result of our navigation pokemons screen is:

![image](https://github.com/carlostom98/Pokedex/assets/66192349/45bd4d08-d46e-491d-8a0a-602975618c74)

![image](https://github.com/carlostom98/Pokedex/assets/66192349/a70bf3b0-500e-40e2-a1d5-e47887f29e78)

*What about Color BackGroundViewModel and PokemonSelectedViewModel?* Take a look into your ScreenComponents.kt and found the fun called 
PokemonEntry()

```kotlin
fun PokemonEntry(
    pokedexModel: PokemonListEntry,
    colorBackGViewModel: ColorBackGroundViewModel = get(),
    modifier: Modifier = Modifier,
) {
    val pokemonsSelected: PokemonsSelectedViewModel = get()
    val databaseViewModel: DataBaseManagerViewModel = get()
    val defaultDominantColor = MaterialTheme.colorScheme.surface
    var dominantColor by remember { mutableStateOf(defaultDominantColor) }
    var selectedState by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(100.dp, RoundedCornerShape(50.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(Brush.verticalGradient(listOf(dominantColor, defaultDominantColor)))
    ) {
        Column {
            SubcomposeAsyncImage(
                ImageRequest.Builder(LocalContext.current)
                    .data(pokedexModel.image)
                    .crossfade(true)
                    .build(),
                contentDescription = pokedexModel.name,
                loading = {
                    CircularProgressIndicator(color = Color.White)
                },
                onSuccess = {
                    colorBackGViewModel.calculateDominantColor(it.result.drawable)
                },
                modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = pokedexModel.name,
                style = TextStyle(Color.Black, fontSize = 20.sp, fontStyle = FontStyle.Italic),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Column(modifier = Modifier.align(BottomEnd)) {
            Row() {
                IconButton(
                    onClick = {
                        selectedState = !selectedState
                        if (selectedState) {
                            pokemonsSelected.addPokemon()
                        } else {
                            pokemonsSelected.removePokemon()
                        }
                        databaseViewModel.addPokemon(pokedexModel)
                    },
                ) {
                    Icon(
                        painter = if (!selectedState) painterResource(id = R.drawable.hearticon_contorn) else painterResource(
                            id = R.drawable.hearticon_red
                        ),
                        contentDescription = "Arrow dropdown",
                        tint = Color.Red,
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
```

You'll see that this @Composable element create our Pokemon Boxes, So this two viewModel classes will make the brush effect and add or remove our selected pokemons to our future groups.

## FIREBASE CONFIGURATION
Get into firebase and create a new proyect:

Follow this steps to connect your firebase and begin a new project to enroll your app:

[Firebase and Android Studio](https://developer.android.com/studio/write/firebase?hl=es-419#:~:text=Primero%2C%20aseg%C3%BArate%20de%20haber%20agregado,una%20de%20las%20funciones%20enumeradas.)

Make sure you have the google.jsn file here in your project view:

![image](https://github.com/carlostom98/Pokedex/assets/66192349/88315211-cf97-4608-aa51-cafa84d9afa5)


## FIREBASE LOGIN SERVICES

First, add the authentication service to your app, next enable the Google and Facebook Login:

![image](https://github.com/carlostom98/Pokedex/assets/66192349/91b02530-3b9d-499f-9914-424608a73ce3)


Add your 'Huella digital' in your project configuration

![image](https://github.com/carlostom98/Pokedex/assets/66192349/fe35cc2e-c8af-47ec-b353-8597de1c57c9)

![image](https://github.com/carlostom98/Pokedex/assets/66192349/4f2d2154-6fd5-4c30-ade0-3ca1634554a5)

Your 'huella digital' will appear when you execute a gradle task with *signingReport* command

![image](https://github.com/carlostom98/Pokedex/assets/66192349/7158bac4-a924-4312-8c69-64cdbe8a773a)

Copy and paste your SHA-1 into firebase console.

Implement the following lines into your build.gradle (project):

```groovy
dependencies {
        classpath 'com.android.tools.build:gradle:7.4.2'
        classpath 'com.google.gms:google-services:4.3.15'
    }
```
Also

```groovy
repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
```

And, into your build.gradle(app)

```groovy
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'com.google.gms.google-services'
}
```

And 

```groovy
 implementation platform('com.google.firebase:firebase-bom:32.0.0')
    implementation 'com.google.firebase:firebase-analytics:21.3.0'
    implementation 'com.google.firebase:firebase-auth:22.0.0'
    implementation 'com.google.android.gms:play-services-auth:20.5.0'
```

I've created my Authentication package:

![image](https://github.com/carlostom98/Pokedex/assets/66192349/f25195e7-8478-443e-bac8-a4425822f6f4)

Contain the business logic to make the google SignIn and SignOut


```kotlin
class GoogleAuthentication() : IAuth {
    private val firebaseAuth = FirebaseAuth.getInstance()
    override fun googleSignIn(credential: AuthCredential): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.signInWithCredential(credential).await()
            emit(Resource.Succes(result))
        }.catch {
            emit(Resource.Error(null, it.message.toString()))
        }
    }

    override  fun googleSignOut():Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            firebaseAuth.signOut()
            emit(Resource.Succes(true))
        }.catch {
            emit(Resource.Error(null, it.message.toString()))
        }
    }
}
```

This class extend from IAuth in case we want to change or add auth modes:

- Email
- GitHub
- Facebook 

We only have to create another Class which extends from IAuth and set the business logic.

Here I also implemented flow to set another way to use suspend funcs, the we go to our DataBaseManagerViewModel and create the functions to serve the results to UI, DON'T FORGET TO ADD TO OUR KOIN MODULES TO MAKE THE DI WITH GET():

```kotkin
val moduleVM = module {
    single { PokemonViewModel()}
    single { SignInViewModel(get())}
    single { GoogleAuthentication() }
    single { DataBaseManagerViewModel(get()) }
    single<DataBaseImplementation>{ DataBaseManagerFirebaseFirestore() }
    viewModel<ColorBackGroundViewModel> ()
}
```

Finally we can go to create our UI, *RegisterScreen*, inject our viewModel: *val signInViewModel: SignInViewModel = get()*


Create your launcher, to launch the implicit intent aiming to google register, look that here is wher we activate our viewModelFunction:

```kotlin
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

```

Launch this event with your onClick google button 
```kotlin
ButtonRegister(text = "Google Register") {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestIdToken(Tools.TOKEN_GOOGLE_CLIENT)
                        .build()
                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    launcher.launch(googleSignInClient.signInIntent)
                }
```

The Tools.TOKEN_GOOGLE_CLIENT

Is your client ID, found it here:


![image](https://github.com/carlostom98/Pokedex/assets/66192349/a2bc8c2b-a426-4980-a3a7-e9e539150868)


![image](https://github.com/carlostom98/Pokedex/assets/66192349/21351df0-982c-4c6f-a732-208fc50d8341)


Create as a constant as the link to API:
```kotlin
object Tools {
    const val urlPokemonApi="https://pokeapi.co/api/v2/"
    const val PAGE_SIZE=20
    const val TOKEN_GOOGLE_CLIENT="262567266085-j7keglmu9o4v2tblgumat7ouvtkf2kr7.apps.googleusercontent.com"
}
```


And make the logic with the coroutine LaunchedEffect, if we can SignIn, the application will navigate to the *ScreenPokedex* using our navHost and our sealed class.

```kotlin
LaunchedEffect(key1 = googleSignInState?.isSignProcessSucces) {
        if(googleSignInState?.isSignProcessSucces !=null){
            Toast.makeText(context, "Sign In Succes", Toast.LENGTH_SHORT).show()
            navHost.navigate(DestinationScreen.PokedexScreen.baseRoute)
        }
    }
```

Here The result in UI:

![image](https://github.com/carlostom98/Pokedex/assets/66192349/ccd9c3b7-28f1-4a82-b917-d2c8e5d5e1ed)

![image](https://github.com/carlostom98/Pokedex/assets/66192349/d243c624-21eb-434c-b224-ea4398f2e294)

![image](https://github.com/carlostom98/Pokedex/assets/66192349/a432ed80-4cc4-42e9-a9a0-7e5041094d37)

Firebase LogedIn users
![image](https://github.com/carlostom98/Pokedex/assets/66192349/c86e6426-836d-4a55-959f-639495881d57)


You can close your account 

![image](https://github.com/carlostom98/Pokedex/assets/66192349/b0883876-4825-4fad-a15f-e4ebe012cc31)

![image](https://github.com/carlostom98/Pokedex/assets/66192349/8a0934ce-543b-4423-a73e-777a8a4ec33c)



*FACEBOOK LOGIN IS NOT IMPLEMENTED YET*


## FIREBASE FIRESTORE DATABASE
First, create our FirestoreDatabase service:

![image](https://github.com/carlostom98/Pokedex/assets/66192349/d35b7116-b0a5-4e4b-beb9-7e35c81035ba)

Then we have to enroll our project follow next steps into your AndroidStudio:

![image](https://github.com/carlostom98/Pokedex/assets/66192349/20437538-d66d-40b3-8dea-296e8ad13f3e)


![image](https://github.com/carlostom98/Pokedex/assets/66192349/21cbf239-04d9-479e-82ab-2fb39ee32979)

Select: *Get started with Java* and follow the instructions:

![image](https://github.com/carlostom98/Pokedex/assets/66192349/6f2b8718-d422-463d-bde8-d8a4117aba0b)

or

```groovy
 implementation 'com.google.firebase:firebase-database-ktx'
```

![image](https://github.com/carlostom98/Pokedex/assets/66192349/7d85ea0e-8a3f-4610-b0e1-8c0100bc02e8)

The Interface will allow us to change between the different implementations of Database, to make DI we set it as a koin module:

```kotlin
single<DataBaseImplementation>{ DataBaseManagerFirebaseFirestore() }
```

This says to the app Which Interface Class implementation have to use, for example if you see the DatabaseRTDB() class, also extends from DataBaseImplementation Interface, if we want to set another data base we just have to change the previos class (DataBaseManagerFirebaseFirestore()) for (DatabaseRTDB) between the keys.


But now we want to use FirestoreFirebase

Our class will save the Business logic:

```kotlin
class DataBaseManagerFirebaseFirestore : DataBaseImplementation {
    private val database = FirebaseFirestore.getInstance()
    private val listOfPokemons = mutableListOf<PokemonListEntry>()
    private val collection = "PokemonGroups"
    override fun addPokemon(pokemonListEntry: PokemonListEntry) {
        listOfPokemons.add(pokemonListEntry)
        Log.d("POKEMONS_ADDED", "${listOfPokemons}")
    }

    override fun removePokemon(pokemonListEntry: PokemonListEntry) {
        listOfPokemons.remove(pokemonListEntry)
        Log.d("POKEMONS_ADDED", "${listOfPokemons}")
    }

    override fun saveData(nameGroup: String) {
        val dataBasePokemon = DataBasePokemon(nameGroup, listOfPokemons)
        val groupDocRef = database.collection(collection).document(dataBasePokemon.groupName)
        val pokemonesList = mutableListOf<Map<String, Any>>()
        dataBasePokemon.pokemonValues?.forEach { pokemon ->
            val pokemonData = hashMapOf(
                "nombre" to pokemon.name,
                "imagen" to pokemon.image
            )
            pokemonesList.add(pokemonData)
        }
        groupDocRef.set(hashMapOf("Pokemones" to pokemonesList))
    }

    override fun removeData() {

    }

    override  fun retrieveData(callback:(List<DataBasePokemon>)->Unit){

        val listToPlot = mutableListOf<DataBasePokemon>()
        database.collection(collection).get().addOnSuccessListener {result->
            for (documentos in result) {
                val pokemonsArray =
                    documentos.get("Pokemones") as? ArrayList<HashMap<String, String>>
                pokemonsArray?.forEach { pokemonesData ->
                    val nombre = pokemonesData["nombre"]
                    val imagen = pokemonesData["imagen"]
                    listToPlot.add(
                        DataBasePokemon(
                            documentos.id,
                            mutableListOf(PokemonListEntry(nombre.toString(), imagen.toString()))
                        )
                    )
                }
            }
            val combinedList = listToPlot.groupBy { it.groupName }.map { (_, group) ->
                val combinedValues = group.flatMap { it.pokemonValues!! }
                DataBasePokemon(group.first().groupName, combinedValues.toMutableList())
            }
            callback(combinedList)
        }
    }
}
```
Basically it will add or remove a pokemon to the previous list item, based in the model data *DataBasePokemon()*

Then you could send this to the database or get information from the data base.


Our *DataBaseManagerViewModel* will serve our retrieve data to our UI, using LiveData as always:

```kotlin
class DataBaseManagerViewModel(private val dbManager:DataBaseImplementation):ViewModel() {
    private val _pokemonFromDB= MutableLiveData<List<DataBasePokemon>>()
    val pokemonFromDB:LiveData<List<DataBasePokemon>> get() = _pokemonFromDB
    fun saveData(nameGroup: String){
        dbManager.saveData(nameGroup)
    }
    fun addPokemon(pokemon:PokemonListEntry){
        dbManager.addPokemon(pokemon)
    }
    fun getAllPokemonsInDB(){
        dbManager.retrieveData {
            it.let {
                _pokemonFromDB.postValue(it)
            }
        }
    }
    fun removePokemon(pokemon:PokemonListEntry){
        dbManager.removePokemon(pokemon)
    }
}
```

Our LastScreen *PokemonInformation*

Show us our different groups from Firestore DataBase. Injecting the viewModel with Koin: (val databaseManager: DataBaseManagerViewModel = get())

And reusing some composable elements.

The achieve results next:

We can set a like to our pokemons, and also we have a TextField to set a Group Name.

![image](https://github.com/carlostom98/Pokedex/assets/66192349/f8535158-ad80-4857-8474-3000ae7ace0c)

When you have selected more than three pokemons, the Create Group Button is enabled.

![image](https://github.com/carlostom98/Pokedex/assets/66192349/b072ed5d-c91a-4002-aca7-49a4eb4946cf)

Set a Group Name to can create your Group.

![image](https://github.com/carlostom98/Pokedex/assets/66192349/bc81bcee-28cb-496b-850e-cf7b6101c268)

ENJOY YOUR APP:

(Data retrieve from FirestoreDatabase)
![image](https://github.com/carlostom98/Pokedex/assets/66192349/26b76e09-f0ea-43b5-acdd-4845f31dd67f)


![image](https://github.com/carlostom98/Pokedex/assets/66192349/99ad199a-8871-420c-8cf9-3d88a4beab5a)


![image](https://github.com/carlostom98/Pokedex/assets/66192349/ea84c3e5-7c5b-42e3-8117-c5c561cc30d5)


Firestore database view


![image](https://github.com/carlostom98/Pokedex/assets/66192349/8ef48309-7064-43be-9b00-7285b9c83ce6)


![image](https://github.com/carlostom98/Pokedex/assets/66192349/542c5131-bd01-456d-9f8f-8594ab5653bf)


![image](https://github.com/carlostom98/Pokedex/assets/66192349/722eca11-0fe6-416b-b9d9-9e7263d5d088)


BUG FIXED, EACH USER CAN TAKE ITS ID TO ACCES TO ITS DATA, RETRIEVING THE user.uID into the class GooglAuthentication:

```kotlin
override fun googleSignIn(credential: AuthCredential): Flow<Resource<LoginResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.signInWithCredential(credential).await()
            val user=firebaseAuth.currentUser
            if(user != null){
                emit(Resource.Succes(LoginResult(result, user.uid)))
                Log.d("USER_ID", user.uid)
            }
        }.catch {
            emit(Resource.Error(null, it.message.toString()))
        }
    }
```

Returning the result and user ID with a data model:

```kotlin
class LoginResult(val result:AuthResult, val userId: String)
```

New Results, implementing the data for each user Id:

### USER 1:

![image](https://github.com/carlostom98/Pokedex/assets/66192349/3bf64a02-336a-4856-8bf5-657fd2db7cd7)


![image](https://github.com/carlostom98/Pokedex/assets/66192349/65236f1e-31dd-44ec-9e3d-38f022530cf8)


![image](https://github.com/carlostom98/Pokedex/assets/66192349/942ae280-4103-4b06-b88e-4b4f7134553c)


![image](https://github.com/carlostom98/Pokedex/assets/66192349/5a45926c-3ab9-49bc-9aa0-26e2eb4e313e)


### USER 2:


![image](https://github.com/carlostom98/Pokedex/assets/66192349/289a2497-aca6-4b5a-a573-e2771f2e5855)


![image](https://github.com/carlostom98/Pokedex/assets/66192349/0c218397-6227-48a3-aba0-8535aa74deca)


![image](https://github.com/carlostom98/Pokedex/assets/66192349/51d9238d-4726-47aa-8816-7e00d848aa28)


![image](https://github.com/carlostom98/Pokedex/assets/66192349/2701ec64-8b73-430e-b2f3-227ff19c4d98)


Both registered:


![image](https://github.com/carlostom98/Pokedex/assets/66192349/a4469f56-5802-411e-83c1-ce91c6401303)





THE APPLICATION HAVE SOME ERRORS SO FAR. 






