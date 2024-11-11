package com.dahlaran.naturaquiz.ui.theme

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object QuizButtonDefaults {
    val MinHeight = 56.dp
    val MinWidth = 120.dp
    val defaultElevation = 6.dp
    val pressedElevation = 8.dp
    val cornerRadius = 28.dp
    val fontSize = 16.sp
    val paddingSmall = 8.dp

    @Composable
    fun buttonColors() = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}