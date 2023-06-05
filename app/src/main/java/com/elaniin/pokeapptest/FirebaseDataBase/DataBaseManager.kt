package com.elaniin.pokeapptest.FirebaseDataBase

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class DataBaseManager {
    private val database = FirebaseFirestore.getInstance()
    fun saveData(dataBasePokemon: DataBasePokemon) {
        database.collection("PokemonGroups").document(dataBasePokemon.groupName)
            .set(hashMapOf("nombre" to dataBasePokemon.pokemonValues.name,
                "imagen" to dataBasePokemon.pokemonValues.image
                ))
    }

    fun deleteData() {

    }

    fun retrieveData() {

    }
}