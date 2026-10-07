package com.example.aichatbot

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aichatbot.ui.theme.AIChatBotTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.aichatbot.navigation.AppNavHost
import com.example.aichatbot.navigation.NavigationItem
import com.example.aichatbot.navigation.Screens

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivityTag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AIChatBotTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { NavigationBarExample(navController = navController) }
                ) { innerPadding ->
                    AppNavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = Screens.Home
                    )
                }
            }
        }

//        startActivity(Intent(this@MainActivity, Profile::class.java))
    }
}

@Composable
fun NavigationBarExample(navController: NavHostController) {
    val navigationItems = listOf(
        NavigationItem(
            title = Screens.Home.title,
            icon = Screens.Home.icon,
            destination = Screens.Home
        ),
        NavigationItem(
            title = Screens.History.title,
            icon = Screens.History.icon,
            destination = Screens.History
        ),
        NavigationItem(
            title = Screens.Chat().title,
            icon = Screens.Chat().icon,
            destination = Screens.Chat()
        ),
        NavigationItem(
            title = Screens.Settings.title,
            icon = Screens.Settings.icon,
            destination = Screens.Settings
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        navigationItems.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.hasRoute(item.destination::class)
            } == true
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.destination) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(imageVector = item.icon, contentDescription = null) },
                label = { Text(item.title) }
            )
        }
    }
}

@Composable
fun SentMessage(message: String) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = message,
            textAlign = TextAlign.End,
            modifier = Modifier
                .widthIn(max = maxWidth * .75f)
                .background(
                    Color.Blue.copy(alpha = .33f), shape = RoundedCornerShape(
                        topStart = 5.dp,
                        topEnd = 5.dp,
                        bottomEnd = 5.dp,
                    )
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun ReceivedMessage(message: String) {
    BoxWithConstraints(
        Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = message.repeat(20),
            textAlign = TextAlign.Start,
            modifier = Modifier
                .widthIn(max = maxWidth * .75f)
                .background(
                    Color.LightGray, shape = RoundedCornerShape(
                        topStart = 5.dp,
                        topEnd = 5.dp,
                        bottomStart = 5.dp,
                    )
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}


// Day 3 -
@Preview
@Composable
fun ModifierPlaygroundScreen() {
    Box(
        Modifier
            .background(Color.LightGray)
            .fillMaxSize()
            .padding(12.dp),
    ) {
        SizedBox(text = "Size", modifier = Modifier.size(300.dp))
        SizedBox(
            text = "Width & Height",
            modifier = Modifier
                .width(100.dp)
                .height(200.dp),
            color = Color.Yellow
        )
        SizedBox(
            text = "FillMaxWidth",
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            color = Color.Green
        )
//        SizedBox(text = "FillMaxHeight", modifier = Modifier.width(100.dp).fillMaxHeight(), color = Color.Green)
//        SizedBox(text = "FillMaxSize", modifier = Modifier.fillMaxSize(), color = Color.Yellow)
//        SizedBox(text = "RequiredSize", modifier = Modifier.requiredSize(300.dp), color = Color.Blue)

//        PaddingOffSetExperiment(padding = 12, xOffset = 50, yOffset = 20)
    }
}

@Composable
fun SizedBox(text: String, modifier: Modifier = Modifier, color: Color = Color.Red) {
    Text(text = text)
    Spacer(modifier = Modifier.height(12.dp))
    Box(modifier = modifier.background(color))
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun PaddingOffSetExperiment(padding: Int = 0, xOffset: Int = 0, yOffset: Int = 0) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
    ) {
        Box(
            Modifier
                .size(100.dp)
                .background(Color.Red)
                .padding(padding.dp)
                .background(Color.Green)
        )
        Box(
            Modifier
                .size(100.dp)
                .background(Color.Yellow)
                .offset(x = xOffset.dp, y = yOffset.dp)
                .background(Color.Blue)
        )
    }
}