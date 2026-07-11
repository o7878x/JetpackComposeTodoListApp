package com.o7878x.todolistapp.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.o7878x.todolistapp.model.TodoGroup
import com.o7878x.todolistapp.model.TodoItem
import com.o7878x.todolistapp.model.TodoPriority
import com.o7878x.todolistapp.model.TodoStatus
import com.o7878x.todolistapp.ui.theme.LargeDp
import com.o7878x.todolistapp.ui.theme.MediumDp
import com.o7878x.todolistapp.ui.theme.TodoItemBackgroundColor
import com.o7878x.todolistapp.ui.theme.TodoItemIconColor
import com.o7878x.todolistapp.ui.theme.TodoItemTextColor
import com.o7878x.todolistapp.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    uuid: String,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    mainViewModel: MainViewModel,
) {
    val todoItem by mainViewModel.getItemByUUID(uuid).collectAsState(initial = null)

    var isEditing by remember { mutableStateOf(false) }
    var editedTitle by remember { mutableStateOf("") }
    var editedStatus by remember { mutableStateOf(TodoStatus.NOT_STARTED) }
    var editedPriority by remember { mutableStateOf(TodoPriority.LOW) }
    var editedGroup by remember { mutableStateOf(TodoGroup.LIFE) }
    var editedDeadline by remember { mutableStateOf(0L) }
    var showDiscardDialog by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // Handle system back press while editing
    BackHandler(enabled = isEditing) {
        showDiscardDialog = true
    }

    // Discard changes confirmation dialog
    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            title = { Text("Discard changes?") },
            text = { Text("You have unsaved changes. Are you sure you want to discard them?") },
            confirmButton = {
                TextButton(onClick = {
                    showDiscardDialog = false
                    isEditing = false
                }) {
                    Text("Discard", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiscardDialog = false }) {
                    Text("Keep editing")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        val item = todoItem
        if (item == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = TodoItemIconColor)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MediumDp),
                verticalArrangement = Arrangement.spacedBy(MediumDp)
            ) {
                // Title
                item {
                    if (isEditing) {
                        OutlinedTextField(
                            value = editedTitle,
                            onValueChange = { editedTitle = it },
                            label = { Text("Title") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TodoItemIconColor,
                                cursorColor = TodoItemIconColor,
                                focusedLabelColor = TodoItemIconColor,
                                unfocusedBorderColor = TodoItemIconColor.copy(alpha = 0.5f),
                            )
                        )
                    } else {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = TodoItemTextColor,
                            modifier = Modifier.padding(horizontal = MediumDp, vertical = LargeDp)
                        )
                    }
                }

                // Status
                item {
                    if (isEditing) {
                        StatusDropdownField(
                            selected = editedStatus,
                            onOptionSelected = { editedStatus = it }
                        )
                    } else {
                        DetailInfoCard(
                            label = "Status",
                            value = formatStatus(item),
                            valueColor = TodoItemIconColor,
                            decoration = if (item.status == TodoStatus.COMPLETED) TextDecoration.LineThrough else null
                        )
                    }
                }

                // Priority
                item {
                    if (isEditing) {
                        EnumDropdownField(
                            label = "Priority",
                            selected = editedPriority,
                            options = TodoPriority.entries,
                            onOptionSelected = { editedPriority = it },
                            formatOption = { it.name }
                        )
                    } else {
                        DetailInfoCard(
                            label = "Priority",
                            value = item.priority.name,
                            valueColor = TodoItemIconColor,
                        )
                    }
                }

                // Group
                item {
                    if (isEditing) {
                        EnumDropdownField(
                            label = "Group",
                            selected = editedGroup,
                            options = TodoGroup.entries,
                            onOptionSelected = { editedGroup = it },
                            formatOption = { it.name }
                        )
                    } else {
                        DetailInfoCard(
                            label = "Group",
                            value = item.group.name,
                            valueColor = TodoItemIconColor,
                        )
                    }
                }

                // Deadline
                item {
                    if (isEditing) {
                        DeadlineEditor(
                            timestamp = editedDeadline,
                            onDateSelected = { editedDeadline = it }
                        )
                    } else {
                        DetailInfoCard(
                            label = "Deadline",
                            value = formatDeadline(item.deadlineTimestamp),
                            valueColor = TodoItemIconColor,
                        )
                    }
                }

                // UUID (always read-only)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = LargeDp),
                        shape = RoundedCornerShape(size = MediumDp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(TodoItemBackgroundColor.copy(alpha = 0.3f))
                                .padding(MediumDp)
                        ) {
                            Text(
                                text = "ID",
                                fontSize = 12.sp,
                                color = TodoItemTextColor.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.uuid,
                                fontSize = 12.sp,
                                color = TodoItemTextColor.copy(alpha = 0.4f)
                            )
                        }
                    }
                }
            }

            // Edit/Save FAB
            FloatingActionButton(
                onClick = {
                    if (isEditing) {
                        // Save changes
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        mainViewModel.updateItem(
                            item.copy(
                                title = editedTitle,
                                status = editedStatus,
                                priority = editedPriority,
                                group = editedGroup,
                                deadlineTimestamp = editedDeadline
                            )
                        )
                        isEditing = false
                    } else {
                        // Enter edit mode — initialize from current item
                        editedTitle = item.title
                        editedStatus = item.status
                        editedPriority = item.priority
                        editedGroup = item.group
                        editedDeadline = item.deadlineTimestamp
                        isEditing = true
                    }
                },
                containerColor = TodoItemBackgroundColor,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(LargeDp)
            ) {
                Icon(
                    imageVector = if (isEditing) Icons.Default.Done else Icons.Default.Create,
                    contentDescription = if (isEditing) "Save" else "Edit",
                    tint = TodoItemIconColor
                )
            }
        }
    }
}

// ── Dropdown for Status (uses display-name formatting) ──

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatusDropdownField(
    selected: TodoStatus,
    onOptionSelected: (TodoStatus) -> Unit,
) {
    val options = TodoStatus.entries.filter { it != TodoStatus.NONE }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = formatStatus(selected),
            onValueChange = {},
            readOnly = true,
            label = { Text("Status") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TodoItemIconColor,
                cursorColor = TodoItemIconColor,
                focusedLabelColor = TodoItemIconColor,
                unfocusedBorderColor = TodoItemIconColor.copy(alpha = 0.5f),
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(formatStatus(option)) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

// ── Generic dropdown for enum fields ──

// ── Generic dropdown for enum fields ──

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> EnumDropdownField(
    label: String,
    selected: T,
    options: List<T>,
    onOptionSelected: (T) -> Unit,
    formatOption: (T) -> String,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = formatOption(selected),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TodoItemIconColor,
                cursorColor = TodoItemIconColor,
                focusedLabelColor = TodoItemIconColor,
                unfocusedBorderColor = TodoItemIconColor.copy(alpha = 0.5f),
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(formatOption(option)) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

// ── Date picker for deadline ──

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeadlineEditor(
    timestamp: Long,
    onDateSelected: (Long) -> Unit,
) {
    var showPicker by remember { mutableStateOf(false) }

    val initialMillis = if (timestamp > 0L) timestamp else null
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

    if (showPicker) {
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onDateSelected(millis)
                    }
                    showPicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPicker = false
                }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Card(
        onClick = { showPicker = true },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(size = MediumDp),
        colors = CardDefaults.cardColors(
            containerColor = TodoItemBackgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LargeDp, vertical = MediumDp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Deadline",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = TodoItemIconColor.copy(alpha = 0.7f),
                modifier = Modifier.width(80.dp)
            )

            HorizontalDivider(
                modifier = Modifier
                    .width(1.dp)
                    .height(24.dp),
                color = TodoItemIconColor.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.width(LargeDp))

            Text(
                text = formatDeadline(timestamp),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = TodoItemIconColor,
            )
        }
    }
}

// ── Read-only info card (existing) ──

@Composable
private fun DetailInfoCard(
    label: String,
    value: String,
    valueColor: Color = TodoItemTextColor,
    decoration: TextDecoration? = null,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = LargeDp),
        shape = RoundedCornerShape(size = MediumDp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(TodoItemBackgroundColor)
                .padding(horizontal = LargeDp, vertical = MediumDp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = valueColor.copy(alpha = 0.7f),
                modifier = Modifier.width(80.dp)
            )

            HorizontalDivider(
                modifier = Modifier
                    .width(1.dp)
                    .height(24.dp),
                color = valueColor.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.width(LargeDp))

            Text(
                text = value,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = valueColor,
                textDecoration = decoration
            )
        }
    }
}

// ── Formatting helpers ──

private fun formatDeadline(timestamp: Long): String {
    return if (timestamp <= 0L) "—" else SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        .format(Date(timestamp))
}

private fun formatStatus(item: TodoItem): String {
    return formatStatus(item.status)
}

private fun formatStatus(status: TodoStatus): String {
    return when (status) {
        TodoStatus.NONE -> "None"
        TodoStatus.NOT_STARTED -> "Not Started"
        TodoStatus.IN_PROGRESS -> "In Progress"
        TodoStatus.COMPLETED -> "Completed"
        TodoStatus.SUSPENDED -> "Suspended"
        TodoStatus.CANCELLED -> "Cancelled"
        TodoStatus.ISSUE -> "Issue"
    }
}