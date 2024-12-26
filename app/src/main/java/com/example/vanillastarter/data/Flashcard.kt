package com.example.uas_mobcomp_test.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "flashcards",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("categoryId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )])
data class Flashcard (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val conclusionText: String,
    val categoryLevel: Int,
    val imagePath: String?,
    val frequency: Int,
    val categoryId: Int,
    val backgroundColor: String,
)