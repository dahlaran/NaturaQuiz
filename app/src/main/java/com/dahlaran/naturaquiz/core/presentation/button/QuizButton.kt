package com.dahlaran.naturaquiz.core.presentation.button

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.dahlaran.naturaquiz.ui.theme.QuizButtonDefaults

@Composable
fun QuizButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .defaultMinSize(minWidth = QuizButtonDefaults.MinWidth, minHeight = QuizButtonDefaults.MinHeight),
        enabled = enabled,
        shape = RoundedCornerShape(QuizButtonDefaults.cornerRadius),
        colors = QuizButtonDefaults.buttonColors(),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = QuizButtonDefaults.defaultElevation,
            pressedElevation = QuizButtonDefaults.pressedElevation
        )
    ) {
        Text(
            text = text,
            fontSize = QuizButtonDefaults.fontSize,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = QuizButtonDefaults.paddingSmall)
        )
    }
}