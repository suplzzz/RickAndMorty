package com.example.rickandmorty.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.rickandmorty.data.CharacterRemoteMediator
import com.example.rickandmorty.data.database.AppDatabase
import com.example.rickandmorty.data.mappers.toDomain
import com.example.rickandmorty.data.network.ApiService
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase
) : CharacterRepository {

    private val characterDao = appDatabase.characterDao()

    @OptIn(ExperimentalPagingApi::class)
    override fun getCharacters(
        nameQuery: String,
        statusQuery: String,
        speciesQuery: String,
        typeQuery: String,
        genderQuery: String
    ): Flow<PagingData<Character>> {

        val pagingSourceFactory = {
            characterDao.getCharacters(
                nameQuery = nameQuery,
                statusQuery = statusQuery,
                speciesQuery = speciesQuery,
                typeQuery = typeQuery,
                genderQuery = genderQuery
            )
        }

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = CharacterRemoteMediator(
                appDatabase = appDatabase,
                apiService = apiService
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingDataEntity ->
            pagingDataEntity.map { characterEntity ->
                characterEntity.toDomain()
            }
        }
    }

    override fun getCharacterDetail(id: Int): Flow<Character?> {
        return characterDao.getCharacterById(id).map { characterEntity ->
            characterEntity?.toDomain()
        }
    }
}