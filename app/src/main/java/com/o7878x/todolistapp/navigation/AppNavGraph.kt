package com.o7878x.todolistapp.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.o7878x.todolistapp.screen.DetailScreen
import com.o7878x.todolistapp.screen.HomeScreen
import com.o7878x.todolistapp.screen.ScreenType
import com.o7878x.todolistapp.viewmodel.MainViewModel
import org.koin.compose.viewmodel.koinViewModel

private const val ANIM_DURATION = 350

@Composable
fun AppNavGraph(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val mainViewModel = koinViewModel<MainViewModel>()

    NavHost(
        navController = navController,
        startDestination = ScreenType.Home,
        modifier = modifier,
        enterTransition = { fadeIn(animationSpec = tween(ANIM_DURATION)) },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it / 3 },
                animationSpec = tween(ANIM_DURATION)
            ) + fadeOut(animationSpec = tween(ANIM_DURATION))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec = tween(ANIM_DURATION)
            ) + fadeIn(animationSpec = tween(ANIM_DURATION))
        },
        popExitTransition = { fadeOut(animationSpec = tween(ANIM_DURATION)) },
    ) {
        composable<ScreenType.Home> {
            HomeScreen(
                innerPadding = innerPadding,
                mainViewModel = mainViewModel,
                onNavigateToDetail = { uuid ->
                    navController.navigate(route = ScreenType.Detail(uuid))
                }
            )
        }

        composable<ScreenType.Detail> { backStackEntry ->
            val detailBean = backStackEntry.toRoute<ScreenType.Detail>()
            DetailScreen(
                uuid = detailBean.uuid,
                innerPadding = innerPadding,
                mainViewModel = mainViewModel
            )
        }
    }
}