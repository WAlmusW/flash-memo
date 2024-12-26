package com.example.uas_mobcomp_test.ui.screen

import android.app.Application
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uas_mobcomp_test.data.Category
import com.example.uas_mobcomp_test.ui.component.CategoryCard
import com.example.uas_mobcomp_test.ui.component.FilterDropdownButton
import com.example.uas_mobcomp_test.ui.viewmodel.CategoryViewModel
import com.example.uas_mobcomp_test.ui.viewmodel.SortType

@Composable
fun MainScreen(
    categoryLevel: Int,
    categoryViewModel: CategoryViewModel,
    openCategory: (Int, Int) -> Unit,
    onAddCategory: (Int?) -> Unit,
    onEditCategory: (Int) -> Unit
) {
    var selectedSortType by remember { mutableStateOf(SortType.NAME_ASC) }
    var searchQuery by remember { mutableStateOf("") }
    val categories by categoryViewModel.categories.collectAsState()

    // Trigger category loading whenever `selectedSortType` changes
    LaunchedEffect(selectedSortType) {
        categoryViewModel.loadCategories(sortType = selectedSortType, categoryLevel = categoryLevel, categoryId = null)
    }

    // Filter categories based on the search query
    val filteredCategories = remember(searchQuery, categories) {
        categories.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Scaffold(
        containerColor = Color.White,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddCategory(null) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Category")
            }
        }
    ) { padding ->
        Box (
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column {
                // SearchBar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search") },
                    shape = RoundedCornerShape(50),
                    trailingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.padding(4.dp))

                // FilterDropDownButton
                FilterDropdownButton(
                    selectedSortType = selectedSortType,
                    onSortTypeSelected = { selectedSortType = it }
                )

                Spacer(modifier = Modifier.padding(4.dp))

                // CardGrid
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    verticalItemSpacing = 8.dp,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    content = {
                        items(filteredCategories.size) { index ->
                            CategoryCard(
                                category = filteredCategories[index],
                                onClick = { openCategory(categoryLevel+1, filteredCategories[index].id) },
                                onEditClick = { onEditCategory(filteredCategories[index].id) }
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    // Mock Application Context
    val mockApplication = androidx.compose.ui.platform.LocalContext.current.applicationContext as Application

    // Mock ViewModel
    val mockViewModel = CategoryViewModel(mockApplication).apply {
        val mockCategories = listOf(
            Category(1, "Category 1", "Sub 1", "Description 1", 1, null, 10, null, "#FFFFFF"),
            Category(2, "Category 2", "Sub 2", "Description 2", 1, null, 20, null, "#EEEEEE"),
            Category(3, "Category 3", "Sub 3", "Description 3", 1, null, 30, null, "#DDDDDD")
        )
        setMockCategories(mockCategories)
    }

    // Composable Preview
    MainScreen(
        categoryLevel = 1,
        categoryViewModel = mockViewModel,
        openCategory = { categoryLevel, id -> /* No-op */ },
        onAddCategory = { categoryId -> /* No-op */ },
        onEditCategory = { /* No-op */ }
    )
}



