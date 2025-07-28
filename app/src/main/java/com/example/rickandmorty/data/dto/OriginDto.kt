package com.example.rickandmorty.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class OriginDto(
    val name: String,
    val url: String,
)