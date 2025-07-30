package com.example.rickandmorty.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.example.rickandmorty.data.mappers.toDomainModel
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    operator fun invoke() : Flow<PagingData<Character>> {
        return repository.getCharacterPagingData().map { pagingData ->
            pagingData.map { characterEntity ->
                characterEntity.toDomainModel()
            }
        }
    }
}