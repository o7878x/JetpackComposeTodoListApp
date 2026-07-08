package com.o7878x.todolistapp.model

import java.util.Date
import java.util.UUID
import java.util.concurrent.TimeUnit

enum class TodoPriority {
    LOW,
    MID,
    HIGH
}

enum class TodoStatus {
    NONE,
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    SUSPENDED,
    CANCELLED,
    ISSUE,
}

enum class TodoGroup {
    LIFE,
    WORK,
    EXERCISE,
    GAME
}

data class TodoItem(
    val uuid: String = UUID.randomUUID().toString(),
    val title: String,
    val status: TodoStatus = TodoStatus.NOT_STARTED,
    val priority: TodoPriority = TodoPriority.LOW,
    val group: TodoGroup = TodoGroup.LIFE,
    val deadlineTimestamp: Long = Date().time + TimeUnit.DAYS.toMillis(7),
    val roomId: Long = 0L,
)