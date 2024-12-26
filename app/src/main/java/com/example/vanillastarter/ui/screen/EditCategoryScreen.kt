package com.example.uas_mobcomp_test.ui.screen

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uas_mobcomp_test.data.Category
import com.example.uas_mobcomp_test.ui.viewmodel.CategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCategoryScreen(
    id: Int,
    categoryViewModel: CategoryViewModel,
    onSave: (Category) -> Unit,
    onCancel: () -> Unit,
    onDelete: (Category) -> Unit // Added onDelete lambda
) {
    val category by categoryViewModel.selectedCategory.collectAsState()

    LaunchedEffect(id) {
        id.let { categoryViewModel.getCategoryDetail(it) }
    }

    var name by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imagePath by remember { mutableStateOf("") }
    var backgroundColor by remember { mutableStateOf("#FFFFFF") }

    LaunchedEffect(category) {
        category?.let {
            name = it.name
            subtitle = it.subtitle.toString()
            description = it.description.toString()
            imagePath = it.imagePath.toString()
            backgroundColor = it.backgroundColor
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Category") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cancel")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        category?.let {
                            val updatedCategory = it.copy(
                                name = name,
                                subtitle = subtitle,
                                description = description,
                                imagePath = imagePath,
                                frequency = 0,
                                backgroundColor = backgroundColor
                            )
                            onSave(updatedCategory)
                        }
                    }) {
                        Text("Save")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Form fields
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = subtitle,
                    onValueChange = { subtitle = it },
                    label = { Text("Subtitle") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = imagePath,
                    onValueChange = { imagePath = it },
                    label = { Text("Image Path") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = backgroundColor,
                    onValueChange = { backgroundColor = it },
                    label = { Text("Background Color") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                TextButton(
                    onClick = { onDelete(category!!) },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Delete")
                }
            }
        }
    }
}



@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun EditCategoryScreenPreview() {
    val mockApplication = androidx.compose.ui.platform.LocalContext.current.applicationContext as Application

    val mockCategoryViewModel = CategoryViewModel(mockApplication).apply {
        setMockCategories(
            listOf(
                Category(
                    id = 1,
                    name = "Sample Category",
                    subtitle = "Sample Subtitle",
                    description = "Sample Description",
                    categoryLevel = 1,
                    imagePath = "path/to/image",
                    frequency = 5,
                    categoryId = null,
                    backgroundColor = "#FFFFFF"
                )
            )
        )
        getCategoryDetail(1)
    }

    EditCategoryScreen(
        id = 1,
        categoryViewModel = mockCategoryViewModel,
        onSave = { /* No-op */ },
        onCancel = { /* No-op */ },
        onDelete = { /* No-op */ }
    )
}
