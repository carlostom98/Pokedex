package com.elaniin.pokeapptest.FirebaseDataBase

import com.google.firebase.firestore.FirebaseFirestore

class DataBaseManager {
    private val db= FirebaseFirestore.getInstance()

    fun saveData(dataBasePokemon: DataBasePokemon){
        db.collection("Pokemon Groups").document(dataBasePokemon.groupName).set{
            hashMapOf("name" to dataBasePokemon.pokemonName)
        }
    }

    fun deleteData(){

    }

    fun retrieveData(){

    }
}