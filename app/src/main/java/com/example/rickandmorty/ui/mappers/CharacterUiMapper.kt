package com.example.rickandmorty.ui.mappers

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.ui.model.CharacterUiModel
import com.example.rickandmorty.ui.utils.toFormattedDate

fun Character.toUiModel(): CharacterUiModel {
    return CharacterUiModel(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        originName = originName,
        locationName = locationName,
        imageUrl = imageUrl,
        episodeCount = episodeCount,
        createdDate = createdDate.toFormattedDate()
    )
}