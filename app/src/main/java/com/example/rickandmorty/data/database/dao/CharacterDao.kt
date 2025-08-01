package com.example.rickandmorty.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.data.database.model.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Query("""
        SELECT * FROM characters
        WHERE
            (:nameQuery = '' OR name LIKE '%' || :nameQuery || '%') AND
            (:statusQuery = '' OR LOWER(status) = LOWER(:statusQuery)) AND
            (:speciesQuery = '' OR LOWER(species) = LOWER(:speciesQuery)) AND
            (:typeQuery = '' OR LOWER(type) = LOWER(:typeQuery)) AND
            (:genderQuery = '' OR LOWER(gender) = LOWER(:genderQuery))
        ORDER BY id ASC
    """)
    fun getCharacters(
        nameQuery: String,
        statusQuery: String,
        speciesQuery: String,
        typeQuery: String,
        genderQuery: String
    ): PagingSource<Int, CharacterEntity>

    @Query("SELECT * FROM characters WHERE id = :id")
    fun getCharacterById(id: Int): Flow<CharacterEntity?>

    @Query("DELETE FROM characters")
    suspend fun clearAllCharacters()
}