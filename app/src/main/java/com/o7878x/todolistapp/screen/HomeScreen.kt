package com.o7878x.todolistapp.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import com.o7878x.todolistapp.R
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.o7878x.todolistapp.model.TodoItem
import com.o7878x.todolistapp.model.TodoItemEvent
import com.o7878x.todolistapp.model.TodoStatus
import com.o7878x.todolistapp.ui.theme.OverlappingHeight
import com.o7878x.todolistapp.viewmodel.MainViewModel
import com.o7878x.todolistapp.widget.TodoInputBar
import com.o7878x.todolistapp.widget.TodoItemsContainer

@Composable
fun HomeScreen(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    mainViewModel: MainViewModel,
    onNavigateToDetail: (String) -> Unit = {},
) {
    Box(
        modifier = Modifier.Companion
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
                    is TodoItemEvent.Click -> {
                        onNavigateToDetail(event.item.uuid)
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
            modifier = Modifier.Companion.align(Alignment.Companion.BottomStart),
            onAddButtonClick = { title ->
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
        title = { Text(stringResource(R.string.home_delete_dialog_title)) },
        text = { Text(stringResource(R.string.home_delete_dialog_body)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.home_delete_dialog_confirm),
                    color = Color.Red
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.home_delete_dialog_dismiss))
            }
        })
}
