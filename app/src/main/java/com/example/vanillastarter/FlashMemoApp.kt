package com.example.uas_mobcomp_test

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.uas_mobcomp_test.ui.screen.AddCategoryScreen
import com.example.uas_mobcomp_test.ui.screen.AddFlashcardScreen
import com.example.uas_mobcomp_test.ui.screen.EditCategoryScreen
import com.example.uas_mobcomp_test.ui.screen.EditFlashcardScreen
import com.example.uas_mobcomp_test.ui.screen.LevelledScreen
import com.example.uas_mobcomp_test.ui.screen.MainScreen
import com.example.uas_mobcomp_test.ui.viewmodel.CategoryViewModel
import com.example.uas_mobcomp_test.ui.viewmodel.FlashcardViewModel

enum class FlashMemoScreen {
    Main,
    Levelled,
    AddCategory,
    AddFlashcard,
    EditCategory,
    EditFlashcard
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashMemoAppBar(
    title: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    currentRoute: String?,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (currentRoute == FlashMemoScreen.Main.name) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Hello Lorem!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground), // Replace with your avatar resource
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                } else {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Transparent),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}

@Composable
fun FlashMemoApp(
    categoryViewModel: CategoryViewModel = viewModel(),
    flashcardViewModel: FlashcardViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                val currentBackStackEntry = navController.currentBackStackEntryAsState().value
                val currentRoute = currentBackStackEntry?.destination?.route
                val canNavigateBack = navController.previousBackStackEntry != null
                val title = when (currentRoute) {
                    FlashMemoScreen.Main.name -> "Hello Lorem!"
                    FlashMemoScreen.Levelled.name -> "Levelled"
                    FlashMemoScreen.AddCategory.name -> "Add Category"
                    FlashMemoScreen.EditCategory.name -> "Edit Category"
                    FlashMemoScreen.AddFlashcard.name -> "Add Flashcard"
                    FlashMemoScreen.EditFlashcard.name -> "Edit Flashcard"
                    else -> "FlashMemo"
                }

                FlashMemoAppBar(
                    title = title,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { navController.popBackStack() },
                    currentRoute = currentRoute
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = FlashMemoScreen.Main.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                // Main screen
                composable(route = FlashMemoScreen.Main.name) {
                    val categoryLevel = 1

                    MainScreen(
                        categoryLevel = categoryLevel,
                        categoryViewModel = categoryViewModel,
                        openCategory = { nextCategoryLevel, id ->
                            navController.navigate("${FlashMemoScreen.Levelled.name}/level=${nextCategoryLevel}/id=${id}")
                        },
                        onAddCategory = { categoryId -> navController.navigate("${FlashMemoScreen.AddCategory.name}/level=${categoryLevel}/id=${categoryId}") },
                        onEditCategory = { id -> navController.navigate("${FlashMemoScreen.EditCategory.name}/id=${id}") }
                    )
                }

                // Add Category screen
                composable(route = "${FlashMemoScreen.AddCategory.name}/level={categoryLevel}/id={categoryId}") { backStackEntry ->
                    val categoryLevel = backStackEntry.arguments?.getString("categoryLevel")!!.toInt()
                    val parentId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull()
                    AddCategoryScreen(
                        categoryLevel = categoryLevel,
                        categoryId = parentId,
                        onSave = { category ->
                            categoryViewModel.insertCategory(category)
                            navController.popBackStack()
                                 },
                        onCancel = { navController.popBackStack() }
                    )
                }

                // Add Flashcard screen
                composable(route = "${FlashMemoScreen.AddFlashcard.name}/level={categoryLevel}/id={categoryId}") { backStackEntry ->
                    val categoryLevel = backStackEntry.arguments?.getString("categoryLevel")!!.toInt()
                    val parentId = backStackEntry.arguments?.getString("categoryId")!!.toInt()
                    AddFlashcardScreen(
                        categoryLevel = categoryLevel,
                        categoryId = parentId,
                        onSave = { flashcard ->
                            flashcardViewModel.insertFlashcard(flashcard)
                            navController.popBackStack()
                                 },
                        onCancel = { navController.popBackStack() }
                    )
                }

                // Edit Category screen
                composable(route = "${FlashMemoScreen.EditCategory.name}/id={id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")!!.toInt()
                    EditCategoryScreen(
                        id = id,
                        categoryViewModel = categoryViewModel,
                        onSave = { category ->
                            categoryViewModel.updateCategory(category)
                            navController.popBackStack()
                                 },
                        onCancel = { navController.popBackStack() },
                        onDelete = { category ->
                            categoryViewModel.deleteCategory(category)
                            navController.popBackStack()
                        }
                    )
                }

                // Edit Flashcard screen
                composable(route = "${FlashMemoScreen.EditFlashcard.name}/id={id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")!!.toInt()
                    EditFlashcardScreen(
                        id = id,
                        flashcardViewModel = flashcardViewModel,
                        onSave = { flashcard ->
                            flashcardViewModel.updateFlashcard(flashcard)
                            navController.popBackStack()
                                 },
                        onCancel = { navController.popBackStack() },
                        onDelete = { flashcard ->
                            flashcardViewModel.deleteFlashcard(flashcard)
                            navController.popBackStack()
                        }
                    )
                }

                // Levelled screen
                composable(route = "${FlashMemoScreen.Levelled.name}/level={categoryLevel}/id={id}") { backStackEntry ->
                    val parentId = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
                    val categoryLevel = backStackEntry.arguments?.getString("categoryLevel")?.toIntOrNull() ?: 0

                    LevelledScreen(
                        parentId = parentId,
                        categoryLevel = categoryLevel,
                        categoryViewModel = categoryViewModel,
                        flashcardViewModel = flashcardViewModel,
                        openCategory = { nextCategoryLevel, id ->
                            navController.navigate("${FlashMemoScreen.Levelled.name}/level=${nextCategoryLevel}/id=${id}")
                        },
                        onEditCategory = { id -> navController.navigate("${FlashMemoScreen.EditCategory.name}/id=${id}") },
                        onEditFlashcard = { id -> navController.navigate("${FlashMemoScreen.EditFlashcard.name}/id=${id}") },
                        onAddCategory = { categoryId -> navController.navigate("${FlashMemoScreen.AddCategory.name}/level=${categoryLevel}/id=${categoryId}") },
                        onAddFlashcard = { categoryId -> navController.navigate("${FlashMemoScreen.AddFlashcard.name}/level=${categoryLevel}/id=${categoryId}") },
                    )
                }
            }
        }
    }
}

