package com.example.rickandmorty.data.mappers

import com.example.rickandmorty.data.database.model.CharacterEntity
import com.example.rickandmorty.data.dto.CharacterDto
import com.example.rickandmorty.domain.model.Character



fun CharacterDto.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        originName = origin.name,
        locationName = location.name,
        imageUrl = image,
        episodeCount = episode.size,
        created = created
    )
}

fun CharacterEntity.toDomain(): Character {
    return Character(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type.ifEmpty { "Unknown" },
        gender = gender,
        originName = originName,
        locationName = locationName,
        imageUrl = imageUrl,
        episodeCount = episodeCount,
        createdDate = created
    )
}