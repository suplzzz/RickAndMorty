package com.example.rickandmorty.data.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.rickandmorty.data.database.AppDatabase
import com.example.rickandmorty.data.database.CharacterEntity
import com.example.rickandmorty.data.database.RemoteKey
import com.example.rickandmorty.data.mappers.toEntity
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase
) : RemoteMediator<Int, CharacterEntity>() {

    private val characterDao = appDatabase.characterDao()
    private val remoteKeyDao = appDatabase.remoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MINUTES.toMillis(15)

        val lastUpdated = remoteKeyDao.getFirstRemoteKey()?.lastUpdated ?: 0L

        return if (System.currentTimeMillis() - lastUpdated < cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKeys = getLastRemoteKey(state)
                    remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = apiService.getCharacters(page = page)
            val endOfPaginationReached = response.info.next == null

            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (endOfPaginationReached) null else page + 1
            val lastUpdated = System.currentTimeMillis()

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    characterDao.clearAll()
                    remoteKeyDao.clearAll()
                }
                val characterEntities = response.results.map { it.toEntity() }
                val keys = response.results.map {
                    RemoteKey(
                        characterId = it.id,
                        prevKey = prevKey,
                        nextKey = nextKey,
                        lastUpdated = lastUpdated
                    )
                }
                characterDao.insertAll(characterEntities)
                remoteKeyDao.insertAll(keys)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, CharacterEntity>): RemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { character -> remoteKeyDao.getRemoteKeyByCharacterId(character.id) }
    }
}