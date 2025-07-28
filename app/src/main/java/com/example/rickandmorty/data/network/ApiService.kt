package com.example.rickandmorty.data.network

import com.example.rickandmorty.data.dto.ApiResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    //Get all characters
    @GET("api/character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ) : ApiResponseDto
}