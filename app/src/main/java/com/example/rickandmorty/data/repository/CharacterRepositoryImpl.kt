package com.example.rickandmorty.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.rickandmorty.data.database.AppDatabase
import com.example.rickandmorty.data.database.CharacterEntity
import com.example.rickandmorty.data.mappers.toDomainModel
import com.example.rickandmorty.data.mappers.toEntity
import com.example.rickandmorty.data.network.ApiService
import com.example.rickandmorty.data.network.CharacterRemoteMediator
import com.example.rickandmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.example.rickandmorty.domain.model.Character
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class CharacterRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase
) : CharacterRepository {

    private val characterDao = appDatabase.characterDao()

    @OptIn(ExperimentalPagingApi::class)
    override fun getCharacterPagingData(): Flow<PagingData<CharacterEntity>> {
        val pagingSourceFactory = { appDatabase.characterDao().pagingSource() }

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = CharacterRemoteMediator(
                apiService,
                appDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override fun getCharacterById(id: Int): Flow<Character> = flow {
        val cachedCharacter = characterDao.getCharacterById(id).first()
        if (cachedCharacter != null) {
            emit(cachedCharacter.toDomainModel())
        }

        try {
            val characterFromApi = apiService.getCharacterById(id)
            characterDao.insertAll(listOf(characterFromApi.toEntity()))

            val updatedCharacter = characterDao.getCharacterById(id).first()
            if (updatedCharacter != null) {
                emit(updatedCharacter.toDomainModel())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}