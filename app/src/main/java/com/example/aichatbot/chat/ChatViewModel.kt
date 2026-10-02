package com.example.aichatbot.chat

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    private val _isChatUpdating = mutableStateOf(false)
    val isChatUpdating: Boolean get() = _isChatUpdating.value

    private val _error = mutableStateOf<ErrorDC?>(null)
    val error: ErrorDC? get() = _error.value

    private var _messages = mutableStateListOf<MessageDC>()
    val messages: List<MessageDC> get() = _messages

    private var _message = mutableStateOf<TextFieldValue>(TextFieldValue(""))
    val message get() = _message.value


    fun sendMessage() {
        viewModelScope.launch {
            try {
                if (message.text.isEmpty()) {
                    showErrorMessage("Please enter the message!", type = ErrorType.ALERT)
                    return@launch
                }

//                throw Exception("This is a test exception")

                _messages.add(MessageDC(message = message.text, sender = UserType.USER))
                _message.value = TextFieldValue("")
                _messages.add(MessageDC(message = "Thinking...", sender = UserType.AI))
                _isChatUpdating.value = true

                delay(1500L)
                _messages.removeLastOrNull()
                _messages.add(
                    MessageDC(
                        message = "Hello, This is a fake AI response",
                        sender = UserType.AI
                    )
                )

                _isChatUpdating.value = false
            } catch (e: Exception) {
                showErrorMessage(e.message ?: "Something Went Wrong!", ErrorType.FAILURE)
            }
        }
    }

    fun loadData() {
        Log.e("TEST_CHECK", "loadData")
        viewModelScope.launch {
            _isLoading.value = true
            delay(2000L)
            _isLoading.value = false
        }
    }

    fun onMessageChanged(msg: TextFieldValue) {
        _message.value = msg
    }

    fun showErrorMessage(message: String, type: ErrorType) {
        _message.value = TextFieldValue("")
        _error.value = ErrorDC(message, type)
    }

    fun dismissErrorDialog() {
        _message.value = TextFieldValue("")
        _error.value = null
    }

    fun retry() {
        dismissErrorDialog()
        loadData()
    }

    fun clearChat() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(500L)
            _messages.clear()
            _isLoading.value = false
        }
    }
}