package com.example.aichatbot.ui.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.aichatbot.ui.theme.AIChatBotTheme

private const val TAG = "ChatScreenTag"

@Composable
fun ChatScreen(
    chatId: String? = null,
    viewModel: ChatViewModel = viewModel(),
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {

    Column() {
        ChatToolbar(
            isEmptyChat = viewModel.messages.isEmpty(),
            clearChat = viewModel::clearChat,
            chatId = chatId,
            navController = navController
        )

        ChatScreenContent(viewModel = viewModel, modifier = modifier)
    }
}

@Composable
private fun ChatScreenContent(
    viewModel: ChatViewModel,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        Log.d(TAG, "ChatScreenContent: Inside Launch Effect")
        viewModel.loadData()
    }

    if (viewModel.isLoading) {
        LoadingIndicator()
    } else {
        Column(
            modifier = modifier
                .background(Color.LightGray)
                .fillMaxSize()
        ) {
            ChatView(
                modifier = Modifier.weight(1f),
                messages = viewModel.messages,
                isChatUpdating = viewModel.isChatUpdating
            )
            ChatInput(
                message = viewModel.message,
                onMessageChanged = viewModel::onMessageChanged,
                sendMessage = viewModel::sendMessage
            )
        }
    }

    if (viewModel.error != null) {
        ErrorDialog(
            error = viewModel.error!!,
            dismissErrorDialog = viewModel::dismissErrorDialog,
            retryAction = viewModel::retry
        )
    }
}

@Composable
fun ErrorDialog(
    error: ErrorDC,
    dismissErrorDialog: () -> Unit,
    retryAction: () -> Unit
) {
    val isFailure = error.type == ErrorType.FAILURE
    val positiveBtnText = if (isFailure) "Retry" else "OK"

    AlertDialog(
        onDismissRequest = dismissErrorDialog,
        title = { Text("Error") },
        text = { Text(error.message) },
        confirmButton = {
            Button(onClick = if (isFailure) retryAction else dismissErrorDialog) {
                Text(positiveBtnText)
            }
        },
        dismissButton = {
            if (isFailure) {
                Button(onClick = dismissErrorDialog) { Text("Cancel") }
            } else null
        }
    )
}

@Composable
private fun LoadingIndicator() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatToolbar(
    isEmptyChat: Boolean,
    clearChat: () -> Unit,
    chatId: String?,
    navController: NavHostController,
) {
    var expandedOptions by rememberSaveable { mutableStateOf(false) }

    TopAppBar(
        title = { Text("AI Chat") },
        navigationIcon = {
            if (chatId != null) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            if (isEmptyChat) return@TopAppBar
            Box(contentAlignment = Alignment.TopEnd) {
                IconButton(onClick = { expandedOptions = !expandedOptions }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Settings")
                }

                DropdownMenu(
                    expanded = expandedOptions,
                    onDismissRequest = { expandedOptions = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    DropdownMenuItem(
                        text = { Text("Clear Chat") },
                        onClick = {
                            expandedOptions = false
                            clearChat()
                        }
                    )
                }
            }
        },
        windowInsets = WindowInsets(0, 0, 0, 0)
    )
}

@Composable
fun ChatView(modifier: Modifier, messages: List<MessageDC>, isChatUpdating: Boolean) {
    Log.d(TAG, "ChatView: Composing")
    if (messages.isEmpty()) {
        Log.d(TAG, "ChatView: Message List Empty")
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No messages yet")
        }
        return
    }

    LazyColumn(
        modifier.fillMaxSize(),
        reverseLayout = true,
        contentPadding = PaddingValues(vertical = 10.dp),
        userScrollEnabled = true,
    ) {
        items(messages.size) { index ->
            if (isChatUpdating && messages[index].sender == UserType.AI) {
                ThinkingBubble()
                return@items
            }
            val message = messages[index].message
            if (messages[index].sender == UserType.USER) SendMessageBubble(message)
            else ReceivedMessageBubble(message)
        }
    }
}

@Composable
fun SendMessageBubble(message: String) {
    Log.d(TAG, "SendMessageBubble: Composing")
    BoxWithConstraints(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = message,
            modifier = Modifier
                .background(
                    Color.Green, shape = RoundedCornerShape(
                        topEnd = 5.dp, topStart = 5.dp, bottomStart = 5.dp
                    )
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .widthIn(max = maxWidth * .75f),
            textAlign = TextAlign.End,
        )
    }
}

@Preview
@Composable
fun ThinkingBubble() {
    Log.e(TAG, "ThinkingBubble: Composing")
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp),
        contentAlignment = Alignment.CenterStart
    ) {

        Row(
            modifier = Modifier
                .background(
                    Color.Gray,
                    shape = RoundedCornerShape(topEnd = 5.dp, topStart = 5.dp, bottomEnd = 5.dp)
                )
                .padding(8.dp)
        ) {
            Box(
                Modifier
                    .padding(horizontal = 4.dp)
                    .background(color = Color.Black, shape = CircleShape)
                    .size(8.dp)
            )
            Box(
                Modifier
                    .padding(horizontal = 4.dp)
                    .background(color = Color.Black, shape = CircleShape)
                    .size(8.dp)
            )
            Box(
                Modifier
                    .padding(horizontal = 4.dp)
                    .background(color = Color.Black, shape = CircleShape)
                    .size(8.dp)
            )
        }
    }
}

@Composable
fun ReceivedMessageBubble(message: String) {
    Log.d(TAG, "ReceivedMessageBubble: Composing")
    BoxWithConstraints(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = message,
            modifier = Modifier
                .background(
                    Color.Yellow, shape = RoundedCornerShape(
                        topEnd = 5.dp, topStart = 5.dp, bottomEnd = 5.dp
                    )
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .widthIn(max = maxWidth * .75f),
            textAlign = TextAlign.Start,
        )
    }
}

@Composable
fun ChatInput(
    message: TextFieldValue,
    onMessageChanged: (TextFieldValue) -> Unit,
    sendMessage: () -> Unit
) {
    Log.e(TAG, "ChatInput: Composing - ${message.text}")
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White),
    ) {
        TextField(
            modifier = Modifier.weight(1f),
            value = message,
            onValueChange = onMessageChanged,
            label = { Text("Type a message") },
            colors = TextFieldDefaults.colors(
                // 1. Change the background container color
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,

                // 2. Remove the indicator underline by making it transparent
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
        )
        Spacer(Modifier.width(16.dp))
        IconButton(onClick = sendMessage) {
            Icon(Icons.Filled.Send, contentDescription = "Send")
        }
    }
}