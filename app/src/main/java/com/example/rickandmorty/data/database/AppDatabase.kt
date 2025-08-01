package com.example.rickandmorty.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rickandmorty.data.database.dao.CharacterDao
import com.example.rickandmorty.data.database.dao.RemoteKeyDao
import com.example.rickandmorty.data.database.model.CharacterEntity
import com.example.rickandmorty.data.database.model.RemoteKey

@Database(
    entities = [CharacterEntity::class, RemoteKey::class],
    version = 8,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}