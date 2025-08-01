package com.example.rickandmorty.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.rickandmorty.ui.screens.detail.CharacterDetailScreen
import com.example.rickandmorty.ui.screens.filter.FilterScreen
import com.example.rickandmorty.ui.screens.list.CharacterListScreen

object Routes {
    const val LIST = "list"
    const val DETAIL = "detail"
    const val FILTER = "filter"
}

object Arguments {
    const val CHARACTER_ID = "characterId"
    const val STATUS = "status"
    const val SPECIES = "species"
    const val TYPE = "type"
    const val GENDER = "gender"
}

@Composable
fun AppNavHost(navController: NavHostController) {

    val listScreenRoute = "${Routes.LIST}?" +
            "${Arguments.STATUS}={${Arguments.STATUS}}&" +
            "${Arguments.SPECIES}={${Arguments.SPECIES}}&" +
            "${Arguments.TYPE}={${Arguments.TYPE}}&" +
            "${Arguments.GENDER}={${Arguments.GENDER}}"

    val detailScreenRoute = "${Routes.DETAIL}/{${Arguments.CHARACTER_ID}}"

    val filterScreenRoute = "${Routes.FILTER}?" +
            "${Arguments.STATUS}={${Arguments.STATUS}}&" +
            "${Arguments.SPECIES}={${Arguments.SPECIES}}&" +
            "${Arguments.TYPE}={${Arguments.TYPE}}&" +
            "${Arguments.GENDER}={${Arguments.GENDER}}"

    NavHost(navController = navController, startDestination = listScreenRoute) {

        composable(
            route = listScreenRoute,
            arguments = listOf(
                navArgument(Arguments.STATUS) { type = NavType.StringType; defaultValue = "" },
                navArgument(Arguments.SPECIES) { type = NavType.StringType; defaultValue = "" },
                navArgument(Arguments.TYPE) { type = NavType.StringType; defaultValue = "" },
                navArgument(Arguments.GENDER) { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            CharacterListScreen(
                onCharacterClick = { characterId ->
                    navController.navigate("${Routes.DETAIL}/$characterId")
                },
                onFilterClick = {
                    val currentStatus = backStackEntry.arguments?.getString(Arguments.STATUS) ?: ""
                    val currentSpecies = backStackEntry.arguments?.getString(Arguments.SPECIES) ?: ""
                    val currentType = backStackEntry.arguments?.getString(Arguments.TYPE) ?: ""
                    val currentGender = backStackEntry.arguments?.getString(Arguments.GENDER) ?: ""
                    val route = "${Routes.FILTER}?" +
                            "${Arguments.STATUS}=$currentStatus&" +
                            "${Arguments.SPECIES}=$currentSpecies&" +
                            "${Arguments.TYPE}=$currentType&" +
                            "${Arguments.GENDER}=$currentGender"
                    navController.navigate(route)
                }
            )
        }

        composable(
            route = detailScreenRoute,
            arguments = listOf(
                navArgument(Arguments.CHARACTER_ID) { type = NavType.IntType }
            )
        ) {
            CharacterDetailScreen(
                onUpClick = { navController.popBackStack() }
            )
        }

        composable(
            route = filterScreenRoute,
            arguments = listOf(
                navArgument(Arguments.STATUS) { type = NavType.StringType; defaultValue = "" },
                navArgument(Arguments.SPECIES) { type = NavType.StringType; defaultValue = "" },
                navArgument(Arguments.TYPE) { type = NavType.StringType; defaultValue = "" },
                navArgument(Arguments.GENDER) { type = NavType.StringType; defaultValue = "" }
            )
        ) {
            FilterScreen(
                onBackPressed = { navController.popBackStack() },
                onApplyFilters = { status, species, type, gender ->
                    val route = "${Routes.LIST}?" +
                            "${Arguments.STATUS}=$status&" +
                            "${Arguments.SPECIES}=$species&" +
                            "${Arguments.TYPE}=$type&" +
                            "${Arguments.GENDER}=$gender"

                    navController.navigate(route) {
                        popUpTo(Routes.LIST) { inclusive = true }
                    }
                }
            )
        }
    }
}