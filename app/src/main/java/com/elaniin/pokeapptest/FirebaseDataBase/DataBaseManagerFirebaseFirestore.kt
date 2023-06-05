package com.elaniin.pokeapptest.FirebaseDataBase

import android.util.Log
import com.elaniin.pokeapptest.Model.PokemonListModel.PokemonListEntry
import com.google.firebase.firestore.FirebaseFirestore


class DataBaseManagerFirebaseFirestore:DataBaseImplementation {
    private val database = FirebaseFirestore.getInstance()
    private val listOfPokemons=mutableListOf<PokemonListEntry>()
    override fun addPokemon(pokemonListEntry: PokemonListEntry) {
        listOfPokemons.add(pokemonListEntry)
        Log.d("POKEMONS_ADDED", "${listOfPokemons}")
    }
    override fun removePokemon(pokemonListEntry: PokemonListEntry) {
        listOfPokemons.remove(pokemonListEntry)
        Log.d("POKEMONS_ADDED", "${listOfPokemons}")
    }
    override fun saveData(nameGroup: String) {
        val dataBasePokemon= DataBasePokemon(nameGroup, listOfPokemons)
        val groupDocRef = database.collection("PokemonGroups").document(dataBasePokemon.groupName)
        val pokemonesList = mutableListOf<Map<String, Any>>()
        dataBasePokemon.pokemonValues?.forEach{ pokemon ->
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

    override fun retrieveData() {

    }
}