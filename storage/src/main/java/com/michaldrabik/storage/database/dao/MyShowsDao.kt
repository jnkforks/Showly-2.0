package com.michaldrabik.storage.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.michaldrabik.storage.database.model.MyShow
import com.michaldrabik.storage.database.model.Show

@Dao
interface MyShowsDao {

  @Query("SELECT * FROM shows INNER JOIN shows_my_shows ON shows_my_shows.id_trakt == shows.id_trakt")
  suspend fun getAll(): List<Show>

  @Query("SELECT * FROM shows INNER JOIN shows_my_shows ON shows_my_shows.id_trakt == shows.id_trakt ORDER BY id DESC")
  suspend fun getAllRecent(): List<Show>

  @Query("SELECT shows.id_trakt FROM shows INNER JOIN shows_my_shows ON shows_my_shows.id_trakt == shows.id_trakt")
  suspend fun getAllTraktIds(): List<Long>

  @Query("SELECT * FROM shows INNER JOIN shows_my_shows ON shows_my_shows.id_trakt == shows.id_trakt WHERE shows.id_trakt == :traktId")
  suspend fun getById(traktId: Long): Show?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(show: MyShow)

  @Query("DELETE FROM shows_my_shows WHERE id_trakt == :traktId")
  suspend fun deleteById(traktId: Long)
}