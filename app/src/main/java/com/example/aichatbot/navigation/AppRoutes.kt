package com.example.aichatbot.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

sealed interface Screens {
    val title: String
    val icon: ImageVector

    @Serializable
    data object Home : Screens {
        @Transient override val title: String = "Home"
        @Transient override val icon: ImageVector = Icons.Default.Home
    }


    @Serializable
    data object History : Screens {
        @Transient override val title: String = "History"
        @Transient override val icon: ImageVector = Icons.Default.Menu
    }

    @Serializable
    data object Settings : Screens {
        @Transient override val title: String = "Settings"
        @Transient override val icon: ImageVector = Icons.Default.Settings
    }

    @Serializable
    data class Chat(val chatId: String? = null) : Screens {
        @Transient override val title: String = "Chat"
        @Transient override val icon: ImageVector = Icons.Default.Email
    }
}

data class NavigationItem<T : Any>(
    val title: String,
    val icon: ImageVector,
    val destination: T
)
