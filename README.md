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







