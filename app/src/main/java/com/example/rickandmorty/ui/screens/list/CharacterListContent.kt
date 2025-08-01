package com.example.rickandmorty.ui.screens.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.rickandmorty.ui.model.CharacterUiModel
import com.example.rickandmorty.ui.screens.list.components.CharacterGrid

@Composable
fun CharacterListContent(
    characters: LazyPagingItems<CharacterUiModel>,
    isFiltering: Boolean,
    onCharacterClick: (Int) -> Unit,
    lazyGridState: LazyGridState
) {
    val isPagingRefreshing = characters.loadState.refresh is LoadState.Loading
    val itemCount = characters.itemCount

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isPagingRefreshing && itemCount == 0 -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            characters.loadState.refresh is LoadState.Error && itemCount == 0 -> {
                val error = (characters.loadState.refresh as LoadState.Error).error
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Error: ${error.localizedMessage ?: "Unknown error"}")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { characters.retry() }) { Text("Retry") }
                }
            }
            !isPagingRefreshing && itemCount == 0 -> {
                Text(
                    "No characters found.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                CharacterGrid(
                    modifier = Modifier.fillMaxSize(),
                    characters = characters,
                    lazyGridState = lazyGridState,
                    isFiltering = isFiltering,
                    onCharacterClick = onCharacterClick
                )
            }
        }
    }
}