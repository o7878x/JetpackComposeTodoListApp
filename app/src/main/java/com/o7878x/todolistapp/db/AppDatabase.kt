package com.o7878x.todolistapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.o7878x.todolistapp.db.todo.TodoDao
import com.o7878x.todolistapp.db.todo.TodoEntity
import com.o7878x.todolistapp.db.user.User
import com.o7878x.todolistapp.db.user.UserDao

const val DATABASE_NAME = "todo_db"

@Database(
    entities = [User::class, TodoEntity::class], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun todoDao(): TodoDao
}

fun createAppDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context, AppDatabase::class.java, DATABASE_NAME
    ).createFromAsset("databases/$DATABASE_NAME")
        .fallbackToDestructiveMigration(false)
        .build()
}