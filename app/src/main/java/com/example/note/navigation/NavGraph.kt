package com.example.note.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.note.screens.HomeScreen
import com.example.note.screens.AddNoteScreen
import com.example.note.screens.NoteDetailScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController)
        }
        composable("add") {
            AddNoteScreen(navController)
        }
        composable("detail/{noteId}") {
            val noteId = it.arguments?.getString("noteId")
            NoteDetailScreen(navController, noteId)
        }
    }
}
