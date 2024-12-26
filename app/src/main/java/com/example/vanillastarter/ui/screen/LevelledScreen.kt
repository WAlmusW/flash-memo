package com.example.uas_mobcomp_test.ui.screen

import android.app.Application
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uas_mobcomp_test.data.Category
import com.example.uas_mobcomp_test.data.Flashcard
import com.example.uas_mobcomp_test.ui.component.CategoryCard
import com.example.uas_mobcomp_test.ui.component.FilterDropdownButton
import com.example.uas_mobcomp_test.ui.component.FlashcardCard
import com.example.uas_mobcomp_test.ui.viewmodel.CategoryViewModel
import com.example.uas_mobcomp_test.ui.viewmodel.FlashcardViewModel
import com.example.uas_mobcomp_test.ui.viewmodel.SortType


sealed class ListItem {
    data class CategoryItem(val category: Category) : ListItem()
    data class FlashcardItem(val flashcard: Flashcard) : ListItem()
}

@Composable
fun LevelledScreen(
    parentId: Int,
    categoryLevel: Int,
    categoryViewModel: CategoryViewModel,
    flashcardViewModel: FlashcardViewModel,
    openCategory: (Int, Int) -> Unit,
    onAddCategory: (Int) -> Unit,
    onAddFlashcard: (Int) -> Unit,
    onEditCategory: (Int) -> Unit,
    onEditFlashcard: (Int) -> Unit
) {
    var selectedSortType by remember { mutableStateOf(SortType.NAME_ASC) }
    var searchQuery by remember { mutableStateOf("") }
    var addButtonRevealed by remember { mutableStateOf(false) }

    var parentCategory by remember { mutableStateOf<Category?>(null) }

    LaunchedEffect(parentId) {
        categoryViewModel.getParentCategory(parentId) {
            parentCategory = it
        }
    }

    val categories by categoryViewModel.categories.collectAsState()
    val flashcards by flashcardViewModel.flashcards.collectAsState()

    // Trigger loading whenever `selectedSortType` changes
    LaunchedEffect(selectedSortType) {
        categoryViewModel.loadCategories(sortType = selectedSortType, categoryLevel = categoryLevel, categoryId = parentId)
    }
    LaunchedEffect(selectedSortType) {
        flashcardViewModel.loadFlashcards(sortType = selectedSortType, categoryLevel = categoryLevel, categoryId = parentId)
    }

    // Filter based on the search query
    val filteredCategories = remember(searchQuery, categories) {
        categories.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }
    val filteredFlashcards = remember(searchQuery, flashcards) {
        flashcards.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    val combinedItems = remember(filteredCategories, filteredFlashcards) {
        (filteredCategories.map { ListItem.CategoryItem(it) } +
                filteredFlashcards.map { ListItem.FlashcardItem(it) })
    }

    Scaffold(
        containerColor = Color.White,
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                AnimatedVisibility(
                    visible = addButtonRevealed,
                    enter = slideInVertically { it + 100 } + fadeIn(),
                    exit = slideOutVertically { it + 100 } + fadeOut()
                ) {
                    FloatingActionButton(
                        onClick = { onAddFlashcard(parentId) },
                        containerColor = MaterialTheme.colorScheme.secondary
                    ) {
                        Icon(Icons.Filled.Create, contentDescription = "Add Flashcard")
                    }
                }

                AnimatedVisibility(
                    visible = addButtonRevealed,
                    enter = slideInVertically { it + 50 } + fadeIn(),
                    exit = slideOutVertically { it + 50 } + fadeOut()
                ) {
                    FloatingActionButton(
                        onClick = { onAddCategory(parentId) },
                        containerColor = MaterialTheme.colorScheme.secondary
                    ) {
                        Icon(Icons.Filled.Email, contentDescription = "Add Category")
                    }
                }

                FloatingActionButton(
                    onClick = { addButtonRevealed = !addButtonRevealed },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = if (addButtonRevealed) Icons.Filled.Close else Icons.Filled.Add,
                        contentDescription = if (addButtonRevealed) "Close Add Menu" else "Open Add Menu"
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search") },
                    shape = RoundedCornerShape(50),
                    trailingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.padding(4.dp))

                FilterDropdownButton(
                    selectedSortType = selectedSortType,
                    onSortTypeSelected = { selectedSortType = it }
                )

                Spacer(modifier = Modifier.padding(4.dp))

                parentCategory?.let { category ->
                    CategoryCard(
                        category = category,
                        onClick = { /* No-op */ },
                        onEditClick = { onEditCategory(category.id) }
                    )
                }

                Spacer(modifier = Modifier.padding(4.dp))

                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    verticalItemSpacing = 8.dp,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    content = {
                        items(combinedItems.size) { index ->
                            when (val listItem = combinedItems[index]) {
                                is ListItem.CategoryItem -> {
                                    CategoryCard(
                                        category = listItem.category,
                                        onClick = { openCategory(categoryLevel+1, listItem.category.id) },
                                        onEditClick = { onEditCategory(listItem.category.id) }
                                    )
                                }
                                is ListItem.FlashcardItem -> {
                                    FlashcardCard(
                                        flashcard = listItem.flashcard,
                                        onEditClick = { onEditFlashcard(listItem.flashcard.id) }
                                    )
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}



@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun LevelledScreenPreview() {
    val mockApplication = androidx.compose.ui.platform.LocalContext.current.applicationContext as Application

    val mockCategoryViewModel = CategoryViewModel(mockApplication).apply {
        val mockCategories = listOf(
            Category(1, "Category 1", "Sub 1", "Description 1", 1, null, 10, null, "#FFFFFF"),
            Category(2, "Category 2", "Sub 2", "Description 2", 1, null, 20, null, "#EEEEEE")
        )
        setMockCategories(mockCategories)
    }

    val mockFlashcardViewModel = FlashcardViewModel(mockApplication).apply {
        val mockFlashcards = listOf(
            Flashcard(1, "Flashcard 1", "Conclusion 1", 1, null, 10, 1, "#FFFFFF"),
            Flashcard(2, "Flashcard 2", "Conclusion 2", 1, null, 20, 2, "#EEEEEE")
        )
        setMockFlashcards(mockFlashcards)
    }

    LevelledScreen(
        parentId = 0,
        categoryLevel = 0,
        categoryViewModel = mockCategoryViewModel,
        flashcardViewModel = mockFlashcardViewModel,
        openCategory = { level, id -> println("Open Category: Level $level, ID $id") },
        onAddCategory = { parentId -> /* No-op */ },
        onAddFlashcard = { parentId -> /* No-op */ },
        onEditCategory = { id -> /* No-op */ },
        onEditFlashcard = { id -> /* No-op */ }
    )
}

