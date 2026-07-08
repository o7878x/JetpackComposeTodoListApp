package com.o7878x.todolistapp

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.o7878x.todolistapp.model.TodoItem
import com.o7878x.todolistapp.model.TodoItemEvent
import com.o7878x.todolistapp.model.TodoStatus
import com.o7878x.todolistapp.ui.theme.OverlappingHeight
import com.o7878x.todolistapp.ui.theme.TodoListAppTheme
import com.o7878x.todolistapp.viewmodel.MainViewModel
import com.o7878x.todolistapp.widget.TodoInputBar
import com.o7878x.todolistapp.widget.TodoItemsContainer
import org.koin.androidx.scope.ScopeActivity
import org.koin.androidx.scope.activityScope
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.scope.Scope
import org.koin.core.scope.ScopeCallback

class MainActivity : ScopeActivity() {
    private val tag = this::class.java.simpleName

    override val scope by activityScope()

    init {
        scope.registerCallback(object : ScopeCallback {
            override fun onScopeClose(scope: Scope) {
                Log.d(tag, "Scope ${scope.id} is closing")
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoListAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppContent(innerPadding)
                }
            }
        }
    }
}

@Composable
fun AppContent(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    mainViewModel: MainViewModel = koinViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        var deletedUUID by remember {
            mutableStateOf<String?>(null)
        }
        TodoItemsContainer(
            todoItemsFlow = mainViewModel.todoListState, onItemEvent = { event ->
                when (event) {
                    is TodoItemEvent.Click -> { /* navigate to detail, etc. */
                    }

                    is TodoItemEvent.Delete -> {
                        deletedUUID = event.item.uuid
                    }

                    is TodoItemEvent.Complete -> mainViewModel.updateItem(
                        event.item.copy(status = TodoStatus.COMPLETED)
                    )

                    is TodoItemEvent.Uncompleted -> mainViewModel.updateItem(
                        event.item.copy(status = TodoStatus.NOT_STARTED)
                    )
                }
            }, overlappingElementsHeight = OverlappingHeight
        )

        TodoInputBar(
            modifier = Modifier.align(Alignment.BottomStart), onAddButtonClick = { title ->
                mainViewModel.addItem(TodoItem(title = title))

                keyboardController?.hide()
                focusManager.clearFocus()
            })

        if (deletedUUID != null) {
            DeleteConfirmationDialog(uuid = deletedUUID!!, onConfirm = {
                mainViewModel.deleteItem(deletedUUID)
                deletedUUID = null
            }, onDismiss = {
                deletedUUID = null
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteConfirmationDialog(uuid: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete item") },
        text = { Text("Are you sure?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Yes", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        })
}
