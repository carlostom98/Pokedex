package com.elaniin.pokeapptest.FirebaseDataBase

import android.util.Log
import androidx.compose.runtime.toMutableStateList
import com.elaniin.pokeapptest.Model.PokemonListModel.PokemonListEntry
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await


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
            Log.d("POKEMONES_LIST", "$listToPlot")
            val combinedList = listToPlot.groupBy { it.groupName }.map { (_, group) ->
                val combinedValues = group.flatMap { it.pokemonValues!! }
                DataBasePokemon(group.first().groupName, combinedValues.toMutableList())
            }
            callback(combinedList)
        }
    }
}
