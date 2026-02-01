package com.example.note.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.note.model.NoteItem
import com.example.note.screens.AddNoteScreen
import com.example.note.screens.HomeScreen
import com.example.note.screens.NoteDetailScreen

@Composable
fun NavGraph(navController: NavHostController) {
    val notes = remember { mutableStateListOf<NoteItem>() }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController = navController, notes = notes)
        }
        composable("add") {
            AddNoteScreen(navController = navController, notes = notes)
        }
        composable("detail/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull()
            val note = notes.find { it.id == noteId }
            NoteDetailScreen(navController = navController, note = note)
        }
    }
}
