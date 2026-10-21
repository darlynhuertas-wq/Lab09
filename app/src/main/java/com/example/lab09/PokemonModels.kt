package com.example.lab09

import com.google.gson.annotations.SerializedName

data class PokemonResponse(val results: List<PokemonResult>)
data class PokemonResult(
    val name: String,
    val url: String
) {

    val imageUrl: String
        get() {
            val id = url.split("/".toRegex()).dropLast(1).last()
            return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
        }
}

data class PokemonDetailModel(
    val id: Int,
    val name: String,
    val weight: Int,
    val height: Int,
    val sprites: Sprites,
    val types: List<TypeSlot>
)

data class Sprites(
    @SerializedName("front_default") val frontDefault: String,
    @SerializedName("other") val other: OtherSprites?
)

data class OtherSprites(
    @SerializedName("official-artwork") val officialArtwork: OfficialArtwork?
)

data class OfficialArtwork(
    @SerializedName("front_default") val frontDefault: String
)

data class TypeSlot(val type: TypeDetail)
data class TypeDetail(val name: String)