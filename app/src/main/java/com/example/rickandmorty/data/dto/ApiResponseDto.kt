package com.example.rickandmorty.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseDto(
    val info: InfoDto,
    val results: List<CharacterDto>
)
