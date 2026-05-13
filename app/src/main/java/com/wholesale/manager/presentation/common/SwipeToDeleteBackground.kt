package com.wholesale.manager.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.SwipeToDismissBoxValue.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteBackground(dismissState: SwipeToDismissBoxState) {
    val direction = dismissState.dismissDirection
    val isDelete = direction == EndToStart
    val isEdit = direction == StartToEnd

    val color = when {
        isDelete -> Color(0xFFBA1A1A)
        isEdit -> Color(0xFF1565C0)
        else -> Color.Transparent
    }
    val alignment = if (isDelete) Alignment.CenterEnd else Alignment.CenterStart
    val icon = if (isDelete) Icons.Filled.Delete else Icons.Filled.Edit

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
            .background(color)
            .padding(horizontal = 20.dp),
        contentAlignment = alignment
    ) {
        if (isDelete || isEdit) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
        }
    }
}
