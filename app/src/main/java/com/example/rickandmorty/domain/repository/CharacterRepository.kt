package com.example.rickandmorty.domain.repository

import androidx.paging.PagingData
import com.example.rickandmorty.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    fun getCharacters(
        nameQuery: String,
        statusQuery: String,
        speciesQuery: String,
        typeQuery: String,
        genderQuery: String
    ): Flow<PagingData<Character>>

    fun getCharacterDetail(id: Int): Flow<Character?>
}