package com.relax.app.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.relax.app.ui.breathe.BreatheScreen
import com.relax.app.ui.components.RelaxBottomBar
import com.relax.app.ui.home.HomeScreen
import com.relax.app.ui.meditate.MeditateScreen
import com.relax.app.ui.player.PlayerScreen
import com.relax.app.ui.sleep.SleepScreen
import com.relax.app.ui.soundscape.SoundscapeScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Meditate : Screen("meditate")
    object Sleep : Screen("sleep")
    object Breathe : Screen("breathe")
    object Soundscape : Screen("soundscape")
    object Player : Screen("player/{contentId}/{contentType}") {
        fun createRoute(contentId: String, contentType: String) =
            "player/$contentId/$contentType"
    }
}

val bottomNavScreens = listOf(
    Screen.Home,
    Screen.Meditate,
    Screen.Sleep,
    Screen.Breathe,
    Screen.Soundscape
)

@Composable
fun RelaxNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in bottomNavScreens.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                RelaxBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { screen ->
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                fadeIn(animationSpec = tween(300)) + slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(300)) + slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            }
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onContentClick = { contentId, contentType ->
                        navController.navigate(Screen.Player.createRoute(contentId, contentType))
                    }
                )
            }
            composable(Screen.Meditate.route) {
                MeditateScreen(
                    onMeditationClick = { meditationId ->
                        navController.navigate(Screen.Player.createRoute(meditationId, "meditation"))
                    }
                )
            }
            composable(Screen.Sleep.route) {
                SleepScreen(
                    onStoryClick = { storyId ->
                        navController.navigate(Screen.Player.createRoute(storyId, "sleep"))
                    }
                )
            }
            composable(Screen.Breathe.route) {
                BreatheScreen()
            }
            composable(Screen.Soundscape.route) {
                SoundscapeScreen()
            }
            composable(
                route = Screen.Player.route,
                arguments = listOf(
                    navArgument("contentId") { type = NavType.StringType },
                    navArgument("contentType") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val contentId = backStackEntry.arguments?.getString("contentId") ?: ""
                val contentType = backStackEntry.arguments?.getString("contentType") ?: ""
                PlayerScreen(
                    contentId = contentId,
                    contentType = contentType,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
