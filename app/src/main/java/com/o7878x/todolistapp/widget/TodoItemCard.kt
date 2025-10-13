package com.o7878x.todolistapp.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.o7878x.todolistapp.R
import com.o7878x.todolistapp.model.TodoItem
import com.o7878x.todolistapp.ui.theme.LargeDp
import com.o7878x.todolistapp.ui.theme.MediumDp
import com.o7878x.todolistapp.ui.theme.TodoItemActionButtonRippleRadius
import com.o7878x.todolistapp.ui.theme.TodoItemBackgroundColor
import com.o7878x.todolistapp.ui.theme.TodoItemHeight
import com.o7878x.todolistapp.ui.theme.TodoItemIconColor
import com.o7878x.todolistapp.ui.theme.TodoItemIconSize
import com.o7878x.todolistapp.ui.theme.TodoItemTextColor
import com.o7878x.todolistapp.ui.theme.TodoItemTitleTextStyle

@Composable
fun TodoItemCard(
    todoItem: TodoItem = TodoItem(title = "Todo Item"),
    onItemClick: (TodoItem) -> Unit = {},
    onItemDelete: (TodoItem) -> Unit = {}
) {
    val backgroundColor =
        if (todoItem.isDone) TodoItemBackgroundColor.copy(alpha = 0.5f) else TodoItemBackgroundColor
    val textColor = if (todoItem.isDone) TodoItemTextColor.copy(alpha = 0.5f) else TodoItemTextColor

    val textDecoration = if (todoItem.isDone) TextDecoration.LineThrough else null
    val iconId =
        if (todoItem.isDone) R.drawable.ic_selected_check_box else R.drawable.ic_empty_check_box
    val iconColorFilter =
        if (todoItem.isDone) ColorFilter.tint(TodoItemIconColor.copy(alpha = 0.5f)) else ColorFilter.tint(
            TodoItemIconColor
        )
    val iconTintColor =
        if (todoItem.isDone) TodoItemIconColor.copy(alpha = 0.5f) else TodoItemIconColor

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(TodoItemHeight),
        elevation = CardDefaults.cardElevation(defaultElevation = LargeDp),
        shape = RoundedCornerShape(size = MediumDp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple()
                ) {
                    onItemClick(todoItem)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconId),
                contentDescription = null,
                modifier = Modifier
                    .padding(MediumDp)
                    .size(TodoItemIconSize),
                colorFilter = iconColorFilter
            )

            Text(
                text = todoItem.title,
                modifier = Modifier.weight(1.0f),
                style = TodoItemTitleTextStyle.copy(color = textColor),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textDecoration = textDecoration
            )

            IconButton(
                onClick = { onItemDelete(todoItem) },
                modifier = Modifier.size(TodoItemActionButtonRippleRadius)
            ) {
                Icon(
                    modifier = Modifier.size(TodoItemIconSize),
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = null,
                    tint = iconTintColor
                )
            }
        }
    }
}

@Preview
@Composable
fun TodoItemCardPreview() {
    Column(
        modifier = Modifier.padding(MediumDp),
        verticalArrangement = Arrangement.spacedBy(MediumDp)
    ) {
        TodoItemCard(todoItem = TodoItem(title = "Wash dishes"))
        TodoItemCard(todoItem = TodoItem(title = "Have lunch", isDone = true))
        TodoItemCard(todoItem = TodoItem(title = "Do homework"))
        TodoItemCard(todoItem = TodoItem(title = "Buy some books", isDone = true))
    }
}