package com.o7878x.todolistapp.model

sealed interface TodoItemEvent {
    val item: TodoItem

    data class Click(override val item: TodoItem) : TodoItemEvent
    data class Delete(override val item: TodoItem) : TodoItemEvent

    data class Complete(override val item: TodoItem) : TodoItemEvent

    data class Uncompleted(override val item: TodoItem) : TodoItemEvent
}
