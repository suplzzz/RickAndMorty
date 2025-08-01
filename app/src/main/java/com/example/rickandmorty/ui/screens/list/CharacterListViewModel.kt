package com.example.rickandmorty.ui.screens.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.rickandmorty.domain.usecase.GetCharactersUseCase
import com.example.rickandmorty.ui.mappers.toUiModel
import com.example.rickandmorty.ui.model.CharacterUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class CharacterListUiState(
    val searchQuery: String = "",
    val statusQuery: String = "",
    val speciesQuery: String = "",
    val typeQuery: String = "",
    val genderQuery: String = ""
) {
    val isFiltering: Boolean
        get() = searchQuery.isNotEmpty() || statusQuery.isNotEmpty() ||
                speciesQuery.isNotEmpty() || typeQuery.isNotEmpty() ||
                genderQuery.isNotEmpty()
}


@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterListUiState())
    val uiState: StateFlow<CharacterListUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                statusQuery = savedStateHandle.get<String>("status") ?: "",
                speciesQuery = savedStateHandle.get<String>("species") ?: "",
                typeQuery = savedStateHandle.get<String>("type") ?: "",
                genderQuery = savedStateHandle.get<String>("gender") ?: ""
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val characters: Flow<PagingData<CharacterUiModel>> = _uiState
        .flatMapLatest { state ->
            getCharactersUseCase(
                nameQuery = state.searchQuery,
                statusQuery = state.statusQuery,
                speciesQuery = state.speciesQuery,
                typeQuery = state.typeQuery,
                genderQuery = state.genderQuery
            )
                .map { pagingData ->
                    pagingData.map { character ->
                        character.toUiModel()
                    }
                }
        }
        .cachedIn(viewModelScope)

    fun onSearchQueryChanged(query: String) {
        _uiState.update { currentState ->
            currentState.copy(searchQuery = query)
        }
    }
}