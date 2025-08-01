package com.example.rickandmorty.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.usecase.GetCharacterDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CharacterDetailUiState {
    data object Loading : CharacterDetailUiState()
    data class Success(val character: Character) : CharacterDetailUiState()
    data class Error(val message: String?) : CharacterDetailUiState()
}

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val getCharacterDetailUseCase: GetCharacterDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterId: Int = checkNotNull(savedStateHandle["characterId"])

    private val _uiState = MutableStateFlow<CharacterDetailUiState>(CharacterDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadCharacterDetails()
    }

    fun loadCharacterDetails() {
        viewModelScope.launch {
            getCharacterDetailUseCase(characterId)
                .onStart { _uiState.value = CharacterDetailUiState.Loading }
                .catch { exception -> _uiState.value = CharacterDetailUiState.Error(exception.message) }
                .collect { character ->
                    if (character != null) {
                        _uiState.value = CharacterDetailUiState.Success(character)
                    } else {
                        _uiState.value = CharacterDetailUiState.Error("Character not found in cache.")
                    }
                }
        }
    }
}