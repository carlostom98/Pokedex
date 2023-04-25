package com.example.dailyjobs.Model.PokemonDataModel

import com.google.gson.annotations.SerializedName

data class PokemonModel(@SerializedName("name") val name: String?, @SerializedName("sprites") val image:Images?, @SerializedName("weight") val weight:String?, @SerializedName("height") val height:String?)
data class Images(@SerializedName("front_default") val defult:String, @SerializedName("back_default") val backDefault:String)
