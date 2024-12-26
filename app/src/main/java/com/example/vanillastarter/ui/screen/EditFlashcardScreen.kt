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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.uas_mobcomp_test.data.Flashcard
import com.example.uas_mobcomp_test.ui.viewmodel.FlashcardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFlashcardScreen(
    id: Int,
    flashcardViewModel: FlashcardViewModel,
    onSave: (Flashcard) -> Unit,
    onCancel: () -> Unit,
    onDelete: (Flashcard) -> Unit
) {
    // Load the flashcard details
    val flashcard by flashcardViewModel.selectedFlashcard.collectAsState()

    // Load the flashcard details if not already loaded
    LaunchedEffect(id) {
        flashcardViewModel.getFlashcardDetail(id)
    }

    // States for form fields
    var name by remember { mutableStateOf("") }
    var conclusionText by remember { mutableStateOf("") }
    var imagePath by remember { mutableStateOf("") }
    var backgroundColor by remember { mutableStateOf("#FFFFFF") }

    // Synchronize form states with the loaded flashcard
    LaunchedEffect(flashcard) {
        flashcard?.let {
            name = it.name
            conclusionText = it.conclusionText
            imagePath = it.imagePath.toString()
            backgroundColor = it.backgroundColor
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Flashcard") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cancel")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        // Save the changes
                        flashcard?.let {
                            val updatedFlashcard = it.copy(
                                name = name,
                                conclusionText = conclusionText,
                                imagePath = imagePath,
                                frequency = 0,
                                backgroundColor = backgroundColor
                            )
                            onSave(updatedFlashcard)
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
                // Name Field
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Conclusion Text Field
                OutlinedTextField(
                    value = conclusionText,
                    onValueChange = { conclusionText = it },
                    label = { Text("Conclusion Text") },
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

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                TextButton(
                    onClick = { onDelete(flashcard!!) },
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
