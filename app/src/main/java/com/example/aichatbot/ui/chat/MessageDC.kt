package com.example.aichatbot.ui.chat

data class MessageDC(
    val message: String,
    val sender: UserType
)

enum class UserType {
    USER,
    AI
}