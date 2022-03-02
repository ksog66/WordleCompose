package com.example.wordle_compose.ui.main

import androidx.annotation.StringRes
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wordle_compose.R
import com.example.wordle_compose.ui.how_to_play.HowToPlayRoute
import com.example.wordle_compose.ui.play.PlayRoute
import com.example.wordle_compose.ui.settings.SettingsRoute
import com.example.wordle_compose.ui.statistics.StatisticsRoute
import kotlinx.coroutines.launch


@Composable
fun WordleHomeScreen() {
    val allScreens = WordleDrawerTab.values().toList()
    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = WordleDrawerTab.fromRoute(backStackEntry.value?.destination?.route)
    val coroutineScope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                allScreens = allScreens,
                onTabSelected = { screen ->
                    navController.navigate(screen.name) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                currentScreen = currentScreen,
                closeDrawer = { coroutineScope.launch { drawerState.close() } }
            )
        }
    ) {
        WordleNavHost(
            navController = navController,
            openDrawer = { coroutineScope.launch { drawerState.open() } }
        )
    }
}

@Composable
fun WordleNavHost(
    navController: NavHostController,
    openDrawer: () -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = WordleDrawerTab.PLAY.name
    ) {
        composable(WordleDrawerTab.PLAY.name) {
            PlayRoute()
        }

        composable(WordleDrawerTab.HOW_TO_PLAY.name) {
            HowToPlayRoute()
        }

        composable(WordleDrawerTab.SETTINGS.name) {
            SettingsRoute()
        }

        composable(WordleDrawerTab.STATISTICS.name) {
            StatisticsRoute()
        }
    }
}

enum class WordleDrawerTab(
    @StringRes val title: Int,
    val icon: ImageVector
) {

    PLAY(R.string.play_title, Icons.Filled.PlayArrow),
    HOW_TO_PLAY(R.string.how_to_play_title, Icons.Outlined.HelpOutline),
    SETTINGS(R.string.settings_title, Icons.Outlined.Settings),
    STATISTICS(R.string.stats_title, Icons.Filled.BarChart);

    companion object {
        fun fromRoute(route: String?): WordleDrawerTab {
            return when (route?.substringBefore("/")) {
                PLAY.name -> PLAY
                HOW_TO_PLAY.name -> HOW_TO_PLAY
                SETTINGS.name -> SETTINGS
                STATISTICS.name -> STATISTICS
                null -> PLAY
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
        }
    }
}