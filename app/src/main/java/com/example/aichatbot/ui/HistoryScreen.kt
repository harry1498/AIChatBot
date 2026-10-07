package com.example.aichatbot.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aichatbot.navigation.Screens

@Composable
fun HistoryScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    ContentView(navController = navController, modifier = modifier)
}
private const val tag = "TEST_CHECK"
private val mList = listOf(
    "One", "Two", "Three", "Four"
)

@Composable
fun ContentView(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    return Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(text = "Good Evening", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "How can I help you?", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button({ Log.e(tag, "onCreate: Writing") }, Modifier.weight(weight = 1f)) {
                Text(text = "Write")
            }
            Spacer(modifier = Modifier.width(20.dp))
            Button({ Log.e(tag, "onCreate: Explaining") }, Modifier.weight(weight = 1f)) {
                Text(text = "Explain")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Recent Conversations")
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn(
            Modifier.weight(weight = 1f),
//                reverseLayout = true,
        ) {
            items(mList.size) { index ->
                val message = mList[index]
                RecentConversations(message)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        Button({
            Log.e(tag, "onCreate: Explaining")
            navController.navigate(Screens.Chat(chatId = "1234567890"))
        }) {
            Row {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add")
                Text(text = "Ask AI")
            }
        }
    }
}

@Composable
fun RecentConversations(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
