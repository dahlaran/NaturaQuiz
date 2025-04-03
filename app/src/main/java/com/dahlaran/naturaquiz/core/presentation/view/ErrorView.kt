package com.dahlaran.naturaquiz.core.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dahlaran.naturaquiz.core.data.AppError
import com.dahlaran.naturaquiz.core.data.ErrorMessageProvider


@Composable
fun ErrorView(
    error: AppError,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val errorMessageProvider = remember { ErrorMessageProvider(context) }
    val errorMessage = errorMessageProvider.getErrorMessage(error)

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            // TODO : Add different icons for different errors
            imageVector = when (error) {
                is AppError.NetworkError,
                is AppError.NoInternetConnection -> Icons.Default.Warning
                is AppError.TimeoutError -> Icons.Default.Warning
                is AppError.ApiError -> Icons.Default.Warning
                is AppError.ResourceNotFoundError -> Icons.Default.Search
                is AppError.ValidationError -> Icons.Default.Warning
                is AppError.DatabaseError -> Icons.Default.Warning
                is AppError.UnexpectedError -> Icons.Default.Warning
                is AppError.EmptyResultError -> Icons.Default.Info
            },
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Preview
@Composable
fun ErrorViewPreview() {
    ErrorView(
        error = AppError.NetworkError(),
        onRetry = {}
    )
}