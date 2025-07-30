package com.example.rickandmorty.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rickandmorty.ui.screens.character_detail.CharacterDetailScreen
import com.example.rickandmorty.ui.screens.character_list.CharacterListScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "character_list"
    ) {
        composable("character_list") {
            CharacterListScreen(
                onCharacterClick = { characterId ->
                    navController.navigate("character_detail/$characterId")
                }
            )
        }
        composable(
            route = "character_detail/{characterId}",
            arguments = listOf(navArgument("characterId") { type = NavType.IntType })
        ) {
            CharacterDetailScreen(
                onUpClick = { navController.popBackStack() }
            )
        }
    }
}