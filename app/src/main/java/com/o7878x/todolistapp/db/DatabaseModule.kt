package com.o7878x.todolistapp.db

import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.single

val databaseModule = module {
    single<AppDatabase> {
        create(::createAppDatabase)
    }

    single<DatabaseApiImpl>() bind DatabaseApi::class
}