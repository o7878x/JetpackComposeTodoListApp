package com.o7878x.todolistapp

import android.app.Application
import com.o7878x.todolistapp.db.databaseModule
import com.o7878x.todolistapp.viewmodel.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TodoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@TodoApplication)
            modules(databaseModule, viewModelModule)
        }
    }
}