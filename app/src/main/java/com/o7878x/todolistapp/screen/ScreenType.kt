package com.o7878x.todolistapp.screen

import kotlinx.serialization.Serializable

sealed class ScreenType {
    @Serializable
    object Home

    @Serializable
    data class Detail(
        val uuid: String
    )
}