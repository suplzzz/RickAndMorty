package com.example.rickandmorty.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.rickandmorty.data.database.AppDatabase
import com.example.rickandmorty.data.database.model.CharacterEntity
import com.example.rickandmorty.data.database.model.RemoteKey
import com.example.rickandmorty.data.mappers.toEntity
import com.example.rickandmorty.data.network.ApiService
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val appDatabase: AppDatabase,
    private val apiService: ApiService
) : RemoteMediator<Int, CharacterEntity>() {

    private val characterDao = appDatabase.characterDao()
    private val remoteKeyDao = appDatabase.remoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        val lastUpdated = remoteKeyDao.getCreationTime() ?: 0L
        val isCacheStale = (System.currentTimeMillis() - lastUpdated) > cacheTimeout

        return if (isCacheStale) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        return try {
            val pageToLoad: Int = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                    prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                    nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
            }

            val response = apiService.getCharacters(
                page = pageToLoad,
                name = "", status = "", species = "", type = "", gender = ""
            )

            val charactersDto = response.results
            val endOfPaginationReached = response.info.next == null

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    characterDao.clearAllCharacters()
                    remoteKeyDao.clearRemoteKeys()
                }

                val prevKey = if (pageToLoad == 1) null else pageToLoad - 1
                val nextKey = if (endOfPaginationReached) null else pageToLoad + 1

                val remoteKeys = charactersDto.map { character ->
                    RemoteKey(id = character.id, prevKey = prevKey, nextKey = nextKey)
                }
                val characterEntities = charactersDto.map { it.toEntity() }

                characterDao.insertAll(characterEntities)
                remoteKeyDao.insertAll(remoteKeys)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            if (e.code() == 404) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, CharacterEntity>): RemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { character -> remoteKeyDao.getRemoteKeyById(character.id) }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, CharacterEntity>): RemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { character -> remoteKeyDao.getRemoteKeyById(character.id) }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, CharacterEntity>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeyDao.getRemoteKeyById(id)
            }
        }
    }
}