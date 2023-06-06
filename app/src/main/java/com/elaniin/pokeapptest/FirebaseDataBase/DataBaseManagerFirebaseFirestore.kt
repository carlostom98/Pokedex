package com.elaniin.pokeapptest.FirebaseDataBase

import android.util.Log
import androidx.compose.runtime.toMutableStateList
import com.elaniin.pokeapptest.Model.PokemonListModel.PokemonListEntry
import com.elaniin.pokeapptest.Tools.Tools
import com.elaniin.pokeapptest.ViewModel.PokemonViewModel.SignInViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import org.koin.androidx.compose.get
import org.koin.androidx.compose.inject


class DataBaseManagerFirebaseFirestore (): DataBaseImplementation {
    private val database = FirebaseFirestore.getInstance()
    private val listOfPokemons = mutableListOf<PokemonListEntry>()

    override fun addPokemon(pokemonListEntry: PokemonListEntry) {
        listOfPokemons.add(pokemonListEntry)
        Log.d("POKEMONS_ADDED", "${listOfPokemons}")
    }


    override fun removePokemon(pokemonListEntry: PokemonListEntry) {
        listOfPokemons.remove(pokemonListEntry)
        Log.d("POKEMONS_ADDED", "${listOfPokemons}")
    }

    override fun saveData(nameGroup: String, userId:String) {
        val dataBasePokemon = DataBasePokemon(nameGroup, listOfPokemons)
        val groupDocRef = database.collection(userId).document(dataBasePokemon.groupName)
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
        listOfPokemons.clear()
    }

    override  fun retrieveData(userId: String,callback:(List<DataBasePokemon>)->Unit){

        val listToPlot = mutableListOf<DataBasePokemon>()
        database.collection(userId).get().addOnSuccessListener {result->
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
