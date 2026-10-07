package com.example.aichatbot.ui.chat

enum class ErrorType {
    FAILURE,
    ALERT,
    NONE
}

data class ErrorDC(val message: String, val type: ErrorType)