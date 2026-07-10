package com.o7878x.todolistapp.screen

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.o7878x.todolistapp.model.TodoItem
import com.o7878x.todolistapp.ui.theme.LargeDp
import com.o7878x.todolistapp.ui.theme.MediumDp
import com.o7878x.todolistapp.ui.theme.TodoItemBackgroundColor
import com.o7878x.todolistapp.ui.theme.TodoItemIconColor
import com.o7878x.todolistapp.ui.theme.TodoItemTextColor
import com.o7878x.todolistapp.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DetailScreen(
    uuid: String,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    mainViewModel: MainViewModel,
) {
    val todoItem by mainViewModel.getItemByUUID(uuid).collectAsState(initial = null)

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
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = TodoItemTextColor,
                        modifier = Modifier.padding(horizontal = MediumDp, vertical = LargeDp)
                    )
                }

                // Status
                item {
                    DetailInfoCard(
                        label = "Status",
                        value = formatStatus(item),
                        valueColor = TodoItemIconColor,
                        decoration = if (item.status.name == "COMPLETED") TextDecoration.LineThrough else null
                    )
                }

                // Priority
                item {
                    DetailInfoCard(
                        label = "Priority",
                        value = item.priority.name,
                        valueColor = TodoItemIconColor,
                    )
                }

                // Group
                item {
                    DetailInfoCard(
                        label = "Group",
                        value = item.group.name,
                        valueColor = TodoItemIconColor,
                    )
                }

                // Deadline
                item {
                    DetailInfoCard(
                        label = "Deadline",
                        value = formatDeadline(item.deadlineTimestamp),
                        valueColor = TodoItemIconColor,
                    )
                }

                // UUID (for reference)
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
        }
    }
}

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

private fun formatDeadline(timestamp: Long): String {
    return if (timestamp <= 0L) "—" else SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        .format(Date(timestamp))
}

private fun formatStatus(item: TodoItem): String {
    return when (item.status.name) {
        "NOT_STARTED" -> "Not Started"
        "IN_PROGRESS" -> "In Progress"
        "COMPLETED" -> "Completed"
        "SUSPENDED" -> "Suspended"
        "CANCELLED" -> "Cancelled"
        "ISSUE" -> "Issue"
        else -> item.status.name
    }
}