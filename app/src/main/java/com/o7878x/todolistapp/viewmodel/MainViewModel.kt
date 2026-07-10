package com.o7878x.todolistapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.o7878x.todolistapp.db.DatabaseApi
import com.o7878x.todolistapp.db.todo.toEntity
import com.o7878x.todolistapp.model.TodoItem
import com.o7878x.todolistapp.model.TodoStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(databaseApi: DatabaseApi) : ViewModel() {
    private val todoRepo = databaseApi.todoRepository

    val todoListState = todoRepo.getAllTodos().map { entities ->
        entities.map { it.toItem() }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        initialValue = emptyList()
    )

    val countState = todoListState.map { it.size }.distinctUntilChanged().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        initialValue = 0
    )

    private val _filter = MutableStateFlow(TodoStatus.NONE)

    val filterState = _filter.asStateFlow()

    val filteredTodoListState = combine(todoListState, _filter) { todoList, filter ->
        when (filter) {
            TodoStatus.NONE -> todoList
            else -> todoList.filter { todo -> todo.status == filter }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        initialValue = emptyList()
    )

    fun updateFilter(filter: TodoStatus) {
        _filter.value = filter
    }

    fun addItem(item: TodoItem) {
        viewModelScope.launch {
            todoRepo.insert(item.toEntity())
        }
    }

    fun deleteItem(uuid: String?) {
        if (uuid == null) {
            return
        }
        viewModelScope.launch {
            todoRepo.delete(uuid)
        }
    }

    fun updateItem(item: TodoItem) {
        viewModelScope.launch {
            todoRepo.update(item.toEntity())
        }
    }

    fun getItemByUUID(uuid: String): Flow<TodoItem?> {
        return todoRepo.getTodoByUUID(uuid).map { it.getOrNull(0)?.toItem() }
    }
}