package com.example.rickandmorty.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.rickandmorty.data.database.AppDatabase
import com.example.rickandmorty.data.database.CharacterEntity
import com.example.rickandmorty.data.network.ApiService
import com.example.rickandmorty.data.network.CharacterRemoteMediator
import com.example.rickandmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase
) : CharacterRepository {

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

}