# Pokedex

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


![image](https://github.com/carlostom98/Pokedex/assets/66192349/ccd9c3b7-28f1-4a82-b917-d2c8e5d5e1ed)

![image](https://github.com/carlostom98/Pokedex/assets/66192349/d243c624-21eb-434c-b224-ea4398f2e294)

![image](https://github.com/carlostom98/Pokedex/assets/66192349/a432ed80-4cc4-42e9-a9a0-7e5041094d37)

Firebase LogedIn users
![image](https://github.com/carlostom98/Pokedex/assets/66192349/c86e6426-836d-4a55-959f-639495881d57)


## FIREBASE FIRESTORE DATABASE
selected button
![image](https://github.com/carlostom98/Pokedex/assets/66192349/f8535158-ad80-4857-8474-3000ae7ace0c)
more than three pokemons
![image](https://github.com/carlostom98/Pokedex/assets/66192349/b072ed5d-c91a-4002-aca7-49a4eb4946cf)
Group Name
![image](https://github.com/carlostom98/Pokedex/assets/66192349/bc81bcee-28cb-496b-850e-cf7b6101c268)
Data collected from Firestore Database
![image](https://github.com/carlostom98/Pokedex/assets/66192349/26b76e09-f0ea-43b5-acdd-4845f31dd67f)
![image](https://github.com/carlostom98/Pokedex/assets/66192349/99ad199a-8871-420c-8cf9-3d88a4beab5a)
Firestore view
![image](https://github.com/carlostom98/Pokedex/assets/66192349/8ef48309-7064-43be-9b00-7285b9c83ce6)
![image](https://github.com/carlostom98/Pokedex/assets/66192349/542c5131-bd01-456d-9f8f-8594ab5653bf)







