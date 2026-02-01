package com.example.note.screens

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.navigation.NavController
import com.example.note.model.NoteItem

@Composable
fun AddNoteScreen(navController: NavController, notes: MutableList<NoteItem>) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val context = LocalContext.current
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFFFF9C4), Color(0xFFFFF59D)) // желто-оранжевый фон
    )

    // Лаунчер камеры
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            imageBitmap = bitmap
        }
    }

    // Launcher для запроса разрешения POST_NOTIFICATIONS (Android 13+)
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Если нужно, можно показать сообщение пользователю, что уведомления не будут
    }

    // Запрашиваем разрешение на Android 13+
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            // Заголовок
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Контент
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Кнопка камеры
            Button(onClick = { launcher.launch(null) }) {
                Text("Take Photo")
            }

            // Превью фото
            imageBitmap?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Кнопка сохранения заметки
            Button(
                onClick = {
                    val id = (notes.maxOfOrNull { it.id } ?: 0) + 1
                    notes.add(NoteItem(id, title, content, imageBitmap))

                    // 🔔 Локальное уведомление
                    val channelId = "notes_channel"

                    // Создаём канал для Android 8+
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val channel = NotificationChannel(
                            channelId,
                            "Notes notifications",
                            NotificationManager.IMPORTANCE_DEFAULT
                        )
                        val notificationManager = context.getSystemService(NotificationManager::class.java)
                        notificationManager.createNotificationChannel(channel)
                    }

                    // Проверяем разрешение и показываем уведомление
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                        context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

                        val notification = NotificationCompat.Builder(context, channelId)
                            .setContentTitle("Note saved")
                            .setContentText(title)
                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                            .build()

                        val notificationManager = context.getSystemService(NotificationManager::class.java)
                        notificationManager.notify(id, notification)
                    }

                    // Навигация на HomeScreen
                    navController.navigate("home") { popUpTo("home") { inclusive = true } }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
            ) {
                Text("Save", color = Color.White)
            }
        }
    }
}
