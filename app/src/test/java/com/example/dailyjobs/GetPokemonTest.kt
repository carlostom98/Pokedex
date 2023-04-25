package com.example.dailyjobs

import com.example.dailyjobs.Model.GetPokemon
import com.example.dailyjobs.Model.PokemonDataModel.PokemonModel
import com.example.dailyjobs.Model.Services
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetPokemonTest{
    @RelaxedMockK
    private lateinit var services: Services

    lateinit var getPokemon: GetPokemon

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        getPokemon= GetPokemon(services)
    }

    @Test
    fun `When the number setted as a paramether is one`()= runBlocking {
        val pokemonByMock=PokemonModel("bulbasaur", null, null, null)
        // Given
        coEvery { services.getPokemonInfo(1) } returns pokemonByMock
        // When
        val realPokemon=getPokemon.invoke(1)
        // Then
        assertEquals(realPokemon, pokemonByMock)
    }
}