package com.example.aichatbot.profile

data class ProfileDC(
    val avatar: Int,
    val coverImage: Int,
    val name: String,
    val userName: String,
    val description: String,
    val postCount: Int,
    val followerCount: Int,
    val followingCount: Int,
    val isFollowed: Boolean,
    val posts: List<Int>,
)