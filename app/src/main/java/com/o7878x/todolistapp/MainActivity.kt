package com.o7878x.todolistapp

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.o7878x.todolistapp.navigation.AppNavGraph
import com.o7878x.todolistapp.ui.theme.TodoListAppTheme
import org.koin.androidx.scope.ScopeActivity
import org.koin.androidx.scope.activityScope
import org.koin.core.scope.Scope
import org.koin.core.scope.ScopeCallback


class MainActivity : ScopeActivity() {
    private val tag = this::class.java.simpleName

    override val scope by activityScope()

    init {
        scope.registerCallback(object : ScopeCallback {
            override fun onScopeClose(scope: Scope) {
                Log.d(tag, "Scope ${scope.id} is closing")
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoListAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavGraph(innerPadding = innerPadding)
                }
            }
        }
    }
}
