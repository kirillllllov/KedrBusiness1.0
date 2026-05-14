package com.wholesale.manager.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.wholesale.manager.domain.model.User
import com.wholesale.manager.presentation.common.AppDropdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(viewModel: UsersViewModel) {
    val state by viewModel.state.collectAsState()

    if (state.showDialog) {
        AddEditUserDialog(
            editing = state.editingUser,
            onDismiss = { viewModel.dismissDialog() },
            onSave = { name, username, password, role ->
                viewModel.save(name, username, password, role)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Пользователи", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddDialog() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Добавить", tint = Color.White)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (state.errorMessage != null) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        state.errorMessage!!,
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            if (state.users.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Нет пользователей", color = MaterialTheme.colorScheme.outline)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.users, key = { it.id }) { user ->
                        UserCard(
                            user = user,
                            onEdit = { viewModel.showEditDialog(user) },
                            onDelete = { viewModel.delete(user) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserCard(user: User, onEdit: () -> Unit, onDelete: () -> Unit) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            icon = {
                Icon(
                    Icons.Filled.Warning, null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Удалить пользователя?") },
            text = { Text("Пользователь «${user.displayName}» будет удалён.") },
            confirmButton = {
                Button(
                    onClick = { onDelete(); showDeleteConfirm = false },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Удалить") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Отмена") }
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(user.displayName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                Text("@${user.username}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                RoleChip(user.role)
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Filled.Edit, null, tint = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = { showDeleteConfirm = true }) {
                Icon(Icons.Filled.Delete, null, tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun RoleChip(role: String) {
    val (label, color) = when (role) {
        User.ROLE_DIRECTOR -> "Директор" to Color(0xFF6A1B9A)
        User.ROLE_ADMIN -> "Администратор" to Color(0xFF1565C0)
        User.ROLE_EXECUTOR -> "Исполнитель" to Color(0xFF2E7D32)
        else -> role to Color.Gray
    }
    Surface(
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(top = 4.dp)
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditUserDialog(
    editing: User?,
    onDismiss: () -> Unit,
    onSave: (displayName: String, username: String, password: String, role: String) -> Unit
) {
    var displayName by remember { mutableStateOf(editing?.displayName ?: "") }
    var username by remember { mutableStateOf(editing?.username ?: "") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf(editing?.role ?: User.ROLE_ADMIN) }

    var nameError by remember { mutableStateOf(false) }
    var usernameError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (editing == null) "Новый пользователь" else "Редактировать пользователя") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = displayName,
                    onValueChange = { displayName = it; nameError = false },
                    label = { Text("Полное имя *") },
                    isError = nameError,
                    supportingText = if (nameError) ({ Text("Обязательное поле") }) else null,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it; usernameError = false },
                    label = { Text("Логин *") },
                    isError = usernameError,
                    supportingText = if (usernameError) ({ Text("Обязательное поле") }) else null,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; passwordError = false },
                    label = { Text(if (editing == null) "Пароль *" else "Новый пароль (оставьте пустым, чтобы не менять)") },
                    isError = passwordError,
                    supportingText = if (passwordError) ({ Text("Обязательное поле") }) else null,
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
                AppDropdown(
                    label = "Роль",
                    selected = role,
                    options = listOf(
                        User.ROLE_DIRECTOR to "Директор",
                        User.ROLE_ADMIN to "Администратор",
                        User.ROLE_EXECUTOR to "Исполнитель"
                    ),
                    onSelected = { role = it }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                nameError = displayName.isBlank()
                usernameError = username.isBlank()
                passwordError = editing == null && password.isBlank()
                if (!nameError && !usernameError && !passwordError) {
                    onSave(displayName, username, password, role)
                }
            }) { Text("Сохранить") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}
