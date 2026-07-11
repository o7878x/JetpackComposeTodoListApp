package com.o7878x.todolistapp.db.todo

import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getAllTodos(): Flow<List<TodoEntity>>

    fun getTodoByUUID(uuid: String): Flow<TodoEntity?>

    suspend fun insert(todoEntity: TodoEntity)

    suspend fun delete(uuid: String)

    suspend fun update(todoEntity: TodoEntity)
}

class TodoRepositoryImpl(private val dao: TodoDao) : TodoRepository {
    override fun getAllTodos(): Flow<List<TodoEntity>> {
        return this.dao.getAllTodos()
    }

    override fun getTodoByUUID(uuid: String): Flow<TodoEntity?> {
        return this.dao.getTodoByUUID(uuid)
    }

    override suspend fun insert(todoEntity: TodoEntity) {
        this.dao.insert(todoEntity)
    }

    override suspend fun delete(uuid: String) {
        this.dao.deleteByUUID(uuid)
    }

    override suspend fun update(todoEntity: TodoEntity) {
        this.dao.update(todoEntity)
    }
}