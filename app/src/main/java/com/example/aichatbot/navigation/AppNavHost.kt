package com.example.aichatbot.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.aichatbot.ui.chat.ChatScreen
import com.example.aichatbot.ui.HistoryScreen
import com.example.aichatbot.ui.HomeScreen
import com.example.aichatbot.ui.SettingsScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: Any = Screens.Home
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<Screens.Home> { HomeScreen() }
        composable<Screens.History> { HistoryScreen(navController = navController) }
        composable<Screens.Settings> { SettingsScreen() }
        composable<Screens.Chat> { backStackEntry ->
            val chatRoute: Screens.Chat = backStackEntry.toRoute()
            ChatScreen(
                chatId = chatRoute.chatId,
                navController = navController
            )
        }
    }
}