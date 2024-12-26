package com.example.uas_mobcomp_test.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uas_mobcomp_test.data.Category
import com.example.uas_mobcomp_test.data.Flashcard

@Composable
fun CategoryCard(
    category: Category,
    onClick: () -> Unit,
    onEditClick: (Category) -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color.LightGray)
            ) {
                category.imagePath?.toIntOrNull()?.let { resourceId ->
                    Image(
                        painter = painterResource(id = resourceId),
                        contentDescription = "Category Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } ?: Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No Image",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                IconButton(
                    onClick = { onEditClick(category) },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Edit")
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp, bottom = 16.dp, end = 16.dp)
            ) {
                Text(text = category.name, style = MaterialTheme.typography.headlineMedium)
                category.subtitle?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
                category.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun FlashcardCard(
    flashcard: Flashcard,
    onEditClick: () -> Unit
) {
    var isFlipped by remember { mutableStateOf(false) }

    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isFlipped = !isFlipped }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(if (isFlipped) Color.DarkGray else Color.Blue)
        ) {
            if (isFlipped) {
                Text(
                    text = flashcard.conclusionText,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    flashcard.imagePath?.toIntOrNull()?.let { resourceId ->
                        Image(
                            painter = painterResource(id = resourceId),
                            contentDescription = "Flashcard Image",
                            modifier = Modifier.size(100.dp)
                        )
                    } ?: Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No Image",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = flashcard.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                }
            }

            IconButton(
                onClick = { onEditClick() },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = "Edit", tint = Color.White)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCategoryCard() {
    val sampleCategory = Category(
        name = "Istilah IT",
        subtitle = "Tech",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
        imagePath = null,
        id = 1,
        categoryLevel = 1,
        frequency = 1,
        categoryId = 1,
        backgroundColor = "White"
    )
    CategoryCard(
        category = sampleCategory,
        onClick = { /* Navigate to details */ },
        onEditClick = { /* Navigate to edit */ }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewFlashcardCard() {
    val sampleFlashcard = Flashcard(
        name = "Hack",
        conclusionText = "A brief description of hacking and its implications in cybersecurity.",
        imagePath = null,
        id = 1,
        categoryLevel = 2,
        frequency = 3,
        categoryId = 1,
        backgroundColor = "Black"
    )
    FlashcardCard(
        flashcard = sampleFlashcard,
        onEditClick = { /* Navigate to edit */ }
    )
}