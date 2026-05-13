package com.wholesale.manager.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorText: String = "",
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        isError = isError,
        supportingText = if (isError && errorText.isNotBlank()) {
            { Text(errorText, color = MaterialTheme.colorScheme.error) }
        } else null,
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        enabled = enabled,
        singleLine = maxLines == 1
    )
}
