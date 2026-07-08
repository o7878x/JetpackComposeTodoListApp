package com.o7878x.todolistapp.db.user

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAllUsers(): Flow<List<User>>

    suspend fun insert(user: User)
}

class UserRepositoryImpl(private val dao: UserDao) : UserRepository {
    override fun getAllUsers(): Flow<List<User>> {
        return dao.getAllUsers()
    }

    override suspend fun insert(user: User) {
        dao.insert(user)
    }
}