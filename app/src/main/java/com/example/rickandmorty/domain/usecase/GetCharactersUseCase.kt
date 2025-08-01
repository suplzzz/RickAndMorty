package com.example.rickandmorty.domain.usecase

import androidx.paging.PagingData
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {

    operator fun invoke(
        nameQuery: String,
        statusQuery: String,
        speciesQuery: String,
        typeQuery: String,
        genderQuery: String
    ): Flow<PagingData<Character>> {
        return characterRepository.getCharacters(
            nameQuery = nameQuery,
            statusQuery = statusQuery,
            speciesQuery = speciesQuery,
            typeQuery = typeQuery,
            genderQuery = genderQuery
        )
    }
}