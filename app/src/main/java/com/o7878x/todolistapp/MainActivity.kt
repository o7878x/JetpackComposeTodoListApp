package com.o7878x.todolistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.o7878x.todolistapp.model.TodoItem
import com.o7878x.todolistapp.ui.theme.OverlappingHeight
import com.o7878x.todolistapp.ui.theme.TodoListAppTheme
import com.o7878x.todolistapp.widget.TodoInputBar
import com.o7878x.todolistapp.widget.TodoItemsContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoListAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppContent()
                }
            }
        }
    }
}

@Composable
fun AppContent() {
    val todoItemsFlow = MutableStateFlow(
        listOf(
            TodoItem(title = "Todo Item 1"),
            TodoItem(title = "Todo Item 2", isDone = true),
            TodoItem(title = "Todo Item 3"),
            TodoItem(title = "Todo Item 4", isDone = true),
        )
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        TodoItemsContainer(
            todoItemsFlow = todoItemsFlow,
            onItemClick = {},
            onItemDelete = { item ->
                if (item.isDone) {
                    return@TodoItemsContainer
                }
                todoItemsFlow.value = todoItemsFlow.value.filter {
                    it != item
                }
            },
            overlappingElementsHeight = OverlappingHeight
        )
        TodoInputBar(
            modifier = Modifier.align(Alignment.BottomStart),
            onAddButtonClick = {}
        )
    }
}
