package com.example.rickandmorty.ui.screens.filter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val initialStatus = savedStateHandle.get<String>("status") ?: ""
    private val initialSpecies = savedStateHandle.get<String>("species") ?: ""
    private val initialType = savedStateHandle.get<String>("type") ?: ""
    private val initialGender = savedStateHandle.get<String>("gender") ?: ""

    private val _status = MutableStateFlow(initialStatus)
    val status = _status.asStateFlow()

    private val _species = MutableStateFlow(initialSpecies)
    val species = _species.asStateFlow()

    private val _type = MutableStateFlow(initialType)
    val type = _type.asStateFlow()

    private val _gender = MutableStateFlow(initialGender)
    val gender = _gender.asStateFlow()


    fun onStatusChanged(newStatus: String) {
        _status.value = if (_status.value == newStatus) "" else newStatus
    }

    fun onSpeciesChanged(newSpecies: String) {
        _species.value = newSpecies
    }

    fun onTypeChanged(newType: String) {
        _type.value = newType
    }

    fun onGenderChanged(newGender: String) {
        _gender.value = if (_gender.value == newGender) "" else newGender
    }
}