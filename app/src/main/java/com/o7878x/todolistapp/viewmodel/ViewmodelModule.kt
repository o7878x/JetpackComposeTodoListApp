package com.o7878x.todolistapp.viewmodel

import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val viewModelModule = module {
    viewModel<MainViewModel>()
}