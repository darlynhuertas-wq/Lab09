package com.example.lab09

import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApiService {
    // Obtenemos los 151 originales
    @GET("pokemon?limit=151")
    suspend fun getPokemonList(): PokemonResponse

    // Obtenemos el detalle por nombre (ej. "ditto")
    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(@Path("name") name: String): PokemonDetailModel
}