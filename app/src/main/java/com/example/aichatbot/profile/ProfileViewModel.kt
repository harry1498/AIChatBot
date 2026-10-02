package com.example.aichatbot.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.aichatbot.R

class ProfileViewModel : ViewModel() {
    private var _profileData by mutableStateOf<ProfileDC?>(null)
    val profileData: ProfileDC? get() = _profileData

    private var _selectedTab by mutableIntStateOf(0)
    val selectedTab: Int get() = _selectedTab

    fun loadData() {
        _profileData = ProfileDC(
            avatar = R.drawable.user,
            coverImage = R.drawable.cover_image,
            name = "Sarah Smith",
            userName = "@sarahs",
            description = "Travel enthusisat | Photographer | Coffee Lover",
            postCount = 128,
            followerCount = 12400,
            followingCount = 52,
            isFollowed = false,
            posts = listOf(
                R.drawable.cover_image,
                R.drawable.cover_image,
                R.drawable.cover_image,
                R.drawable.cover_image,
                R.drawable.cover_image
            ),
        )
    }

    fun updateTab(tab: Int) {
        _selectedTab = tab
    }

    fun followUser() {
        Log.e("TEST_CHECK", "followUser: ", )
        _profileData = profileData?.copy(isFollowed = true)
    }

    fun unfollowUser() {
        _profileData = profileData?.copy(isFollowed = false)
    }
}