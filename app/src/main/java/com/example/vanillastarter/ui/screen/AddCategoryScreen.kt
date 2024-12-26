package com.example.uas_mobcomp_test.ui.screen

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uas_mobcomp_test.data.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryScreen(
    categoryLevel: Int,
    categoryId: Int?,
    onSave: (Category) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imagePath by remember { mutableStateOf("") }
    var backgroundColor by remember { mutableStateOf("#FFFFFF") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Category") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cancel")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        val newCategory = Category(
                            name = name,
                            subtitle = subtitle,
                            description = description,
                            categoryLevel = categoryLevel,
                            imagePath = imagePath,
                            frequency = 0,
                            categoryId = categoryId,
                            backgroundColor = backgroundColor
                        )
                        onSave(newCategory)
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
                // Name Field
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Subtitle Field
                OutlinedTextField(
                    value = subtitle,
                    onValueChange = { subtitle = it },
                    label = { Text("Subtitle") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Description Field
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Image Path Field
                OutlinedTextField(
                    value = imagePath,
                    onValueChange = { imagePath = it },
                    label = { Text("Image Path") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Background Color Field
                OutlinedTextField(
                    value = backgroundColor,
                    onValueChange = { backgroundColor = it },
                    label = { Text("Background Color") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun AddCategoryScreenPreview() {
    AddCategoryScreen(
        categoryLevel = 1,
        categoryId = 1,
        onSave = { println("Category added!") },
        onCancel = { println("Add canceled!") }
    )
}
