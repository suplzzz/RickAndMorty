package com.example.rickandmorty.ui.screens.character_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.usecase.GetCharacterDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val getCharacterDetailUseCase: GetCharacterDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterId: Int = checkNotNull(savedStateHandle["characterId"])

    private val _characterState = MutableStateFlow<Character?>(null)
    val characterState = _characterState.asStateFlow()

    init {
        loadCharacterDetails()
    }

    private fun loadCharacterDetails() {
        viewModelScope.launch {
            getCharacterDetailUseCase(characterId)
                .collect { character ->
                    _characterState.value = character
                }
        }
    }
}