package com.example.note.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.note.model.NoteItem

@Composable
fun HomeScreen(navController: NavController, notes: List<NoteItem>) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add") }) {
                Text("+")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(notes) { note ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { navController.navigate("detail/${note.id}") },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = note.title, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = note.content, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
                    }
                }
            }
        }
    }
}
