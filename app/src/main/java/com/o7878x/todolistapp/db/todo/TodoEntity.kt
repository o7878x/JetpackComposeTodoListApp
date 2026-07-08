package com.o7878x.todolistapp.db.todo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.o7878x.todolistapp.model.TodoGroup
import com.o7878x.todolistapp.model.TodoItem
import com.o7878x.todolistapp.model.TodoPriority
import com.o7878x.todolistapp.model.TodoStatus
import java.util.UUID

const val TODO_TABLE_NAME = "todos"

@Entity(tableName = TODO_TABLE_NAME)
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    var uuid: String = "",
    var title: String = "",
    var status: Int = TodoStatus.NOT_STARTED.ordinal,
    var priority: Int = TodoPriority.LOW.ordinal,
    var group: Int = TodoGroup.LIFE.ordinal,
    var deadlineTimestamp: Long = 0L,
) {
    fun toItem(): TodoItem = TodoItem(
        uuid = uuid.ifBlank { UUID.randomUUID().toString() },
        title = title,
        status = TodoStatus.entries.getOrElse(status) { TodoStatus.NOT_STARTED },
        priority = TodoPriority.entries.getOrElse(priority) { TodoPriority.LOW },
        group = TodoGroup.entries.getOrElse(group) { TodoGroup.LIFE },
        deadlineTimestamp = deadlineTimestamp,
        roomId = id,
    )
}

fun TodoItem.toEntity(): TodoEntity = TodoEntity(
    id = roomId,
    uuid = uuid,
    title = title,
    status = status.ordinal,
    priority = priority.ordinal,
    group = group.ordinal,
    deadlineTimestamp = deadlineTimestamp,
)