package com.example.rickandmorty.data.network

import com.example.rickandmorty.data.dto.ApiResponseDto
import com.example.rickandmorty.data.dto.CharacterDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("api/character")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("status") status: String,
        @Query("species") species: String,
        @Query("type") type: String,
        @Query("gender") gender: String
    ): ApiResponseDto

    // В текущей offline-first архитектуре этот эндпоинт не используется напрямую
    // для отображения данных на детальном экране. Вместо этого, UI всегда читает данные из
    // локальной базы данных (Room), которая является "Единым Источником Правды".
    //
    // Этот метод сохраняется для полноты API-контракта и для возможных будущих
    // сценариев (например, для реализации "принудительного обновления" на детальном экране).

    @GET("api/character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): CharacterDto
}