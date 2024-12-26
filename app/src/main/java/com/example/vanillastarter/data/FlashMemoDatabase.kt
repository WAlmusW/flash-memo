package com.example.uas_mobcomp_test.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Category::class, Flashcard::class], version = 1, exportSchema = false)
abstract class FlashMemoDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun flashcardDao(): FlashcardDao

    companion object {
        @Volatile
        private var Instance: FlashMemoDatabase? = null

        fun getDatabase(context: Context): FlashMemoDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, FlashMemoDatabase::class.java, "flashmemo_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}