package com.example.rickandmorty.data.dto

data class ApiResponseDto(
    val infoDto: InfoDto,
    val results: List<CharacterDto>
)
