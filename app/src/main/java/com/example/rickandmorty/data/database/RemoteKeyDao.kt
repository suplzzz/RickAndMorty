package com.example.rickandmorty.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKey>)

    @Query("SELECT * FROM remote_keys WHERE characterId = :id")
    suspend fun getRemoteKeyByCharacterId(id: Int): RemoteKey?

    @Query("SELECT * FROM remote_keys ORDER BY lastUpdated DESC LIMIT 1")
    suspend fun getFirstRemoteKey(): RemoteKey?

    @Query("DELETE FROM remote_keys")
    suspend fun clearAll()
}