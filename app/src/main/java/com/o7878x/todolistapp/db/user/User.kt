package com.o7878x.todolistapp.db.user

import androidx.room.Entity
import androidx.room.PrimaryKey

const val USER_TABLE_NAME = "users"

@Entity(tableName = USER_TABLE_NAME)
class User {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L

    var name: String = "Unknown"
}