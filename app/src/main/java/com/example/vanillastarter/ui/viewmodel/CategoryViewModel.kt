package com.example.uas_mobcomp_test.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas_mobcomp_test.data.Category
import com.example.uas_mobcomp_test.data.CategoryDao
import com.example.uas_mobcomp_test.data.FlashMemoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    private val categoryDao: CategoryDao = FlashMemoDatabase.getDatabase(application).categoryDao()
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> get() = _categories

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> get() = _selectedCategory

    fun loadCategories(sortType: SortType, categoryLevel: Int, categoryId: Int?) {
        viewModelScope.launch {
            val sortedCategories = when (sortType) {
                SortType.NAME_ASC -> categoryDao.getCategoriesSortNameAsc(categoryLevel, categoryId)
                SortType.NAME_DESC -> categoryDao.getCategoriesSortNameDesc(categoryLevel, categoryId)
                SortType.FREQUENCY_ASC -> categoryDao.getCategoriesSortFrequencyAsc(categoryLevel, categoryId)
                SortType.FREQUENCY_DESC -> categoryDao.getCategoriesSortFrequencyDesc(categoryLevel, categoryId)
            }
            sortedCategories.collect { _categories.value = it }
        }
    }

    fun getCategoryDetail(id: Int) {
        viewModelScope.launch {
            categoryDao.getCategoryDetail(id).collect { _selectedCategory.value = it }
        }
    }

    fun insertCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) { categoryDao.insert(category) }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) { categoryDao.update(category) }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) { categoryDao.delete(category) }
    }


    fun setMockCategories(mockCategories: List<Category>) {
        _categories.value = mockCategories
    }

    fun getParentCategory(parentId: Int, onResult: (Category?) -> Unit) {
        viewModelScope.launch {
            categoryDao.getCategoryDetail(parentId).collect { category ->
                onResult(category)
            }
        }
    }
}

