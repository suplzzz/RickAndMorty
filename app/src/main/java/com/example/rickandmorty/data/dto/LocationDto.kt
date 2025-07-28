package com.example.rickandmorty.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    val name: String,
    val url: String,
)