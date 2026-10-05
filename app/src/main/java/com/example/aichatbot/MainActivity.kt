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
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.aichatbot.Day_7.RecompositionPlayground
import com.example.aichatbot.chat.ChatScreen

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivityTag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AIChatBotTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RecompositionPlayground(innerPadding)
//                    ContentView()
//                    ModifierPlaygroundScreen()
                }
            }
        }

//        startActivity(Intent(this@MainActivity, Profile::class.java))

    }

    private val mList = listOf<String>(
        "One", "Two", "Three", "Four"
    )

    //    @Preview
    @Composable
    fun ContentView() {
        return Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
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
                Button({ Log.e(TAG, "onCreate: Writing") }, Modifier.weight(weight = 1f)) {
                    Text(text = "Write")
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button({ Log.e(TAG, "onCreate: Explaining") }, Modifier.weight(weight = 1f)) {
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
//                    if (index % 2 == 0) SentMessage(message)
//                    else ReceivedMessage(message)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            Button({
                Log.e(TAG, "onCreate: Explaining")
                startActivity(Intent(this@MainActivity, ChatScreen::class.java))
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
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
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
                        Color.Blue.copy(alpha = .33f),
                        shape = RoundedCornerShape(
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
                        Color.LightGray,
                        shape = RoundedCornerShape(
                            topStart = 5.dp,
                            topEnd = 5.dp,
                            bottomStart = 5.dp,
                        )
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
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