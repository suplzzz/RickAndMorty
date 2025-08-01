package com.example.rickandmorty.ui.screens.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    onCharacterClick: (Int) -> Unit,
    onFilterClick: () -> Unit,
    vm: CharacterListViewModel = hiltViewModel()
) {
    val characters = vm.characters.collectAsLazyPagingItems()
    val uiState by vm.uiState.collectAsState()

    val lazyGridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    var isGestureRefreshing by remember { mutableStateOf(false) }
    val isPagingRefreshing = characters.loadState.refresh is LoadState.Loading

    LaunchedEffect(isPagingRefreshing) {
        if (!isPagingRefreshing) {
            isGestureRefreshing = false
        }
    }

    LaunchedEffect(uiState, isPagingRefreshing) {
        if (!isPagingRefreshing && characters.itemCount > 0) {
            coroutineScope.launch {
                lazyGridState.scrollToItem(index = 0)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rick and Morty Characters") },
                actions = {
                    IconButton(onClick = onFilterClick) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = vm::onSearchQueryChanged,
                label = { Text("Search by name...") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(32.dp),
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { vm.onSearchQueryChanged("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear text")
                        }
                    }
                }
            )

            PullToRefreshBox(
                isRefreshing = isGestureRefreshing,
                onRefresh = {
                    isGestureRefreshing = true
                    characters.refresh()
                },
                modifier = Modifier.fillMaxSize()
            ) {
                CharacterListContent(
                    characters = characters,
                    isFiltering = uiState.isFiltering,
                    onCharacterClick = onCharacterClick,
                    lazyGridState = lazyGridState
                )
            }
        }
    }
}