package com.example.lab09

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@Composable
fun ScreenPokemonList(navController: NavHostController, servicio: PokemonApiService) {
    val listaPokemon = remember { mutableStateListOf<PokemonResult>() }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val respuesta = servicio.getPokemonList()
            listaPokemon.addAll(respuesta.results)
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            items(listaPokemon) { pokemon ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { navController.navigate("pokemonVer/${pokemon.name}") },
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = pokemon.imageUrl,
                            contentDescription = pokemon.name,
                            modifier = Modifier.size(70.dp).background(Color(0xFFF0F0F0), RoundedCornerShape(35.dp)).padding(8.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = pokemon.name.replaceFirstChar { it.uppercase() },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF333333)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScreenPokemonDetail(servicio: PokemonApiService, name: String) {
    var pokemon by remember { mutableStateOf<PokemonDetailModel?>(null) }

    LaunchedEffect(Unit) {
        try {
            pokemon = servicio.getPokemonDetail(name)
        } catch (e: Exception) { }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        pokemon?.let { pkmn ->
            // Imagen de alta calidad
            val imgUrl = pkmn.sprites.other?.officialArtwork?.frontDefault ?: pkmn.sprites.frontDefault

            Box(modifier = Modifier.size(250.dp).padding(16.dp)) {
                AsyncImage(
                    model = imgUrl,
                    contentDescription = pkmn.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            Text(text = pkmn.name.uppercase(), fontSize = 32.sp, fontWeight = FontWeight.Black)

            // Tipos del Pokemon
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                pkmn.types.forEach { typeSlot ->
                    AssistChip(
                        onClick = { },
                        label = { Text(typeSlot.type.name.uppercase(), fontWeight = FontWeight.Bold) },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Estadísticas
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Pokedex ID: #${pkmn.id}", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
                    Text("Peso: ${pkmn.weight / 10.0} kg", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
                    Text("Altura: ${pkmn.height / 10.0} m", fontSize = 18.sp)
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}