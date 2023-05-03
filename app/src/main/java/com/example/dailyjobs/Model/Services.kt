package com.example.dailyjobs.Model

import com.example.dailyjobs.Model.Pokemon2Model.PokedexProperties
import com.example.dailyjobs.Model.Pokemon2Model.PokemonList
import com.example.dailyjobs.Resource

class Services(private val retrofitService: ApiService) {
    suspend fun getPokemonInfo(id: Int): Resource<PokedexProperties> {
        val response= try {
            retrofitService.getPokemon(id)
        }catch (e:Exception){
            return Resource.Error("Failed trying to retrieve the Pokemon specific data from the API")
        }
        return Resource.Succes(response)
    }
    suspend fun getPokemonList(limit:Int, offset:Int):Resource<PokemonList>{
        val response= try{
            retrofitService.getPokemonList(limit,offset)
        }catch(e:Exception){
            return Resource.Error("Failed trying to retrieve the Pokemon List")
        }
        return Resource.Succes(response)
    }
}