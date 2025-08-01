package com.example.rickandmorty.ui.model

data class CharacterUiModel(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val originName: String,
    val locationName: String,
    val imageUrl: String,
    val episodeCount: Int,
    val createdDate: String
)