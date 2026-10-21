package com.example.lab09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProgPrincipalPokemon()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgPrincipalPokemon() {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val servicio = retrofit.create(PokemonApiService::class.java)
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pokédex Pro", color = Color.White, fontWeight = FontWeight.Black) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFE3350D)) // Rojo Nintendo
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            NavHost(navController = navController, startDestination = "pokemonList") {
                composable("pokemonList") {
                    ScreenPokemonList(navController, servicio)
                }
                composable(
                    "pokemonVer/{name}",
                    arguments = listOf(navArgument("name") { type = NavType.StringType })
                ) {
                    val name = it.arguments?.getString("name") ?: "ditto"
                    ScreenPokemonDetail(servicio, name)
                }
            }
        }
    }
}