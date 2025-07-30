package com.example.rickandmorty.domain.repository

import androidx.paging.PagingData
import com.example.rickandmorty.data.database.CharacterEntity
import com.example.rickandmorty.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharacterPagingData() : Flow<PagingData<CharacterEntity>>

    fun getCharacterById(id: Int): Flow<Character>
}