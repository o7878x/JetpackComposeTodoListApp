package com.o7878x.todolistapp.db

import android.content.Context
import com.o7878x.todolistapp.db.todo.TodoRepository
import com.o7878x.todolistapp.db.todo.TodoRepositoryImpl
import com.o7878x.todolistapp.db.user.UserRepository
import com.o7878x.todolistapp.db.user.UserRepositoryImpl

interface DatabaseApi {
    val userRepository: UserRepository

    val todoRepository: TodoRepository
}

class DatabaseApiImpl(private val db: AppDatabase) : DatabaseApi {
    override val userRepository: UserRepository by lazy {
        UserRepositoryImpl(db.userDao())
    }

    override val todoRepository: TodoRepository by lazy {
        TodoRepositoryImpl(db.todoDao())
    }
}