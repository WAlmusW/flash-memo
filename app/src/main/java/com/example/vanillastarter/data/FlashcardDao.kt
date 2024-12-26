package com.example.uas_mobcomp_test.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Flashcard)

    @Update
    suspend fun update(item: Flashcard)

    @Delete
    suspend fun delete(item: Flashcard)

    @Query("SELECT * from flashcards WHERE id = :id")
    fun getFlashcardDetail(id:  Int): Flow<Flashcard>

    @Query("SELECT * from flashcards WHERE categoryLevel = :categoryLevel AND (:categoryId IS NULL OR categoryId = :categoryId) ORDER BY name ASC")
    fun getFlashcardsSortNameAsc(categoryLevel: Int, categoryId: Int?): Flow<List<Flashcard>>

    @Query("SELECT * from flashcards WHERE categoryLevel = :categoryLevel AND (:categoryId IS NULL OR categoryId = :categoryId) ORDER BY name DESC")
    fun getFlashcardsSortNameDesc(categoryLevel: Int, categoryId: Int?): Flow<List<Flashcard>>

    @Query("SELECT * from flashcards WHERE categoryLevel = :categoryLevel AND (:categoryId IS NULL OR categoryId = :categoryId) ORDER BY frequency ASC")
    fun getFlashcardsSortFrequencyAsc(categoryLevel: Int, categoryId: Int?): Flow<List<Flashcard>>

    @Query("SELECT * from flashcards WHERE categoryLevel = :categoryLevel AND (:categoryId IS NULL OR categoryId = :categoryId) ORDER BY frequency DESC")
    fun getFlashcardsSortFrequencyDesc(categoryLevel: Int, categoryId: Int?): Flow<List<Flashcard>>

}