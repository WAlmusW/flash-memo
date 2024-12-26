package com.example.uas_mobcomp_test.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas_mobcomp_test.data.FlashMemoDatabase
import com.example.uas_mobcomp_test.data.Flashcard
import com.example.uas_mobcomp_test.data.FlashcardDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FlashcardViewModel(application: Application) : AndroidViewModel(application) {

    private val flashcardDao: FlashcardDao = FlashMemoDatabase.getDatabase(application).flashcardDao()
    private val _flashcards = MutableStateFlow<List<Flashcard>>(emptyList())
    val flashcards: StateFlow<List<Flashcard>> get() = _flashcards

    private val _selectedFlashcard = MutableStateFlow<Flashcard?>(null)
    val selectedFlashcard: StateFlow<Flashcard?> get() = _selectedFlashcard

    fun loadFlashcards(sortType: SortType, categoryLevel: Int, categoryId: Int?) {
        viewModelScope.launch {
            val sortedFlashcards = when (sortType) {
                SortType.NAME_ASC -> flashcardDao.getFlashcardsSortNameAsc(categoryLevel, categoryId)
                SortType.NAME_DESC -> flashcardDao.getFlashcardsSortNameDesc(categoryLevel, categoryId)
                SortType.FREQUENCY_ASC -> flashcardDao.getFlashcardsSortFrequencyAsc(categoryLevel, categoryId)
                SortType.FREQUENCY_DESC -> flashcardDao.getFlashcardsSortFrequencyDesc(categoryLevel, categoryId)
            }
            sortedFlashcards.collect { _flashcards.value = it }
        }
    }

    fun getFlashcardDetail(id: Int) {
        viewModelScope.launch {
            flashcardDao.getFlashcardDetail(id).collect { _selectedFlashcard.value = it }
        }
    }

    fun insertFlashcard(flashcard: Flashcard) {
        viewModelScope.launch(Dispatchers.IO) { flashcardDao.insert(flashcard) }
    }

    fun updateFlashcard(flashcard: Flashcard) {
        viewModelScope.launch(Dispatchers.IO) { flashcardDao.update(flashcard) }
    }

    fun deleteFlashcard(flashcard: Flashcard) {
        viewModelScope.launch(Dispatchers.IO) { flashcardDao.delete(flashcard) }
    }


    fun setMockFlashcards(mockFlashcards: List<Flashcard>) {
        _flashcards.value = mockFlashcards
    }
}

