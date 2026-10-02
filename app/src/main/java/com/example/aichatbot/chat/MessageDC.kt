package com.example.aichatbot.chat

data class MessageDC(
    val message: String,
    val sender: UserType
)

enum class UserType {
    USER,
    AI
}