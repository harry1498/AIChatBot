package com.example.aichatbot.ui.chat

sealed class NetworkState {
    data class Success(val data: Any) : NetworkState()
    data class Error(val message: String) : NetworkState()
    object Loading : NetworkState()
    object Ideal : NetworkState()
}