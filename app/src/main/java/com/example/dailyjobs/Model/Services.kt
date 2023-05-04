package com.example.dailyjobs.Model

import com.example.dailyjobs.Model.Pokemon2Model.PokedexProperties
import com.example.dailyjobs.Model.Pokemon2Model.PokemonList
import com.example.dailyjobs.Resource

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