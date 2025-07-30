package com.example.rickandmorty.data.network

import com.example.rickandmorty.data.dto.ApiResponseDto
import com.example.rickandmorty.data.dto.CharacterDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //Get all characters
    @GET("api/character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ) : ApiResponseDto

    //Get character details
    @GET("api/character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): CharacterDto
}