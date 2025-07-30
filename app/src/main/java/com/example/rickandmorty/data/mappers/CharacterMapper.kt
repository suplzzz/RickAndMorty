package com.example.rickandmorty.data.mappers

import android.util.Log
import com.example.rickandmorty.data.database.CharacterEntity
import com.example.rickandmorty.data.dto.CharacterDto
import com.example.rickandmorty.domain.model.Character
import java.text.SimpleDateFormat
import java.util.Locale


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

fun CharacterEntity.toDomainModel(): Character {

    fun formatDate(inputDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val date = inputFormat.parse(inputDate)
            date?.let { outputFormat.format(it) } ?: inputDate
        } catch (e: Exception) {
            Log.e("CharacterEntity","Failed to parse date string: '$inputDate'", e)
            inputDate
        }
    }

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
        // Форматируем дату для отображения
        createdDate = formatDate(created)
    )
}