package com.example.aichatbot.profile

import android.R.attr.maxHeight
import android.os.Bundle
import android.provider.CalendarContract.Colors
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.aichatbot.R
import com.example.aichatbot.ui.theme.AIChatBotTheme
import kotlinx.coroutines.SupervisorJob
import java.util.Vector

class Profile : ComponentActivity() {
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaunchedEffect(SupervisorJob()) { viewModel.loadData() }
            AIChatBotTheme {
                Scaffold(Modifier.fillMaxSize(), topBar = {
                    ProfileToolbar(backAction = { finish() }, profile = viewModel.profileData)
                }) { innerPadding ->
                    ContentView(
                        modifier = Modifier.padding(innerPadding),
                        profile = viewModel.profileData,
                        currentTab = viewModel.selectedTab,
                        followUser = viewModel::followUser,
                        unFollowUser = viewModel::unfollowUser,
                        updateTab = viewModel::updateTab
                    )
                }
            }
        }
    }
}

@Composable
private fun ContentView(
    modifier: Modifier = Modifier,
    profile: ProfileDC?,
    currentTab: Int,
    followUser: () -> Unit,
    unFollowUser: () -> Unit,
    updateTab: (tab: Int) -> Unit
) {
    if (profile == null) return

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            profile.name,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .offset(x = 120.dp)
                .align(Alignment.Start)
        )
        Text(
            profile.userName,
            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            profile.description,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
        )
        Spacer(Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            ProfileStats(
                modifier = Modifier.weight(1f),
                stats = formatCount(profile.postCount),
                title = "Posts"
            )
            VerticalDivider(
                thickness = 1.dp,
                color = Color.Gray.copy(alpha = .5f),
                modifier = Modifier.height(30.dp)
            )
            ProfileStats(
                modifier = Modifier.weight(1f),
                stats = formatCount(profile.followerCount),
                title = "Followers"
            )
            VerticalDivider(
                thickness = 1.dp,
                color = Color.Gray.copy(alpha = .5f),
                modifier = Modifier.height(30.dp)
            )
            ProfileStats(
                modifier = Modifier.weight(1f),
                stats = formatCount(profile.followingCount),
                title = "Following"
            )
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = if (profile.isFollowed) unFollowUser else followUser,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = "Follow", Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text(if (profile.isFollowed) "Unfollow" else "Follow")
            }
        }

        Spacer(Modifier.height(16.dp))
        ProfileTabs(currentTab = currentTab, updateTab = updateTab)
        Spacer(Modifier.height(16.dp))
        GridView(currentTab = currentTab, posts = profile.posts)
    }
}

fun formatCount(value: Int): String {
    fun format(number: Double, suffix: String): String {
        val formatted = String.format("%.1f", number)
            .removeSuffix(".0")

        return "$formatted$suffix"
    }

    return when {
        value >= 1_000_000_000 -> format(value / 1_000_000_000.0, "B")
        value >= 1_000_000 -> format(value / 1_000_000.0, "M")
        value >= 1_000 -> format(value / 1_000.0, "K")
        else -> value.toString()
    }
}

@Composable
fun GridView(currentTab: Int, posts: List<Int>) {
    when (currentTab) {
        0 -> Posts(posts)
        1 -> Reels()
        2 -> Favourites()
    }
}

@Composable
fun Posts(posts: List<Int>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(posts.size) {index->
            val post = posts[index]
            Image(
                painterResource(post),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth()
                    .aspectRatio(1.1f),
            )
        }
    }
}

@Composable
fun Reels() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Reels")
    }
}

@Composable
fun Favourites() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Favourites")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTabs(currentTab: Int, updateTab: (tab: Int) -> Unit) {
    val indicatorColor = Color(0xFF673AB7)

    PrimaryTabRow(
        selectedTabIndex = currentTab,
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.Transparent,
        contentColor = Color.Black,
        indicator = {
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(currentTab)
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(indicatorColor, shape = RoundedCornerShape(3.dp))
            )
        }) {
        TabItem(
            selected = currentTab == 0,
            onClick = { updateTab(0) },
            icon = Icons.Filled.Home,
        )
        TabItem(
            selected = currentTab == 1,
            onClick = { updateTab(1) },
            icon = Icons.Filled.Email,
        )
        TabItem(
            selected = currentTab == 2,
            onClick = { updateTab(2) },
            icon = Icons.Filled.Favorite,
        )
    }
}

@Composable
fun TabItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    indicatorColor: Color = Color(0xFF673AB7)
) {
    val tint = if (selected) indicatorColor else Color.Gray
    Tab(selected = selected, onClick = onClick) {
        Icon(
            icon,
            tint = tint,
            contentDescription = "Posts",
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
fun ProfileStats(modifier: Modifier, stats: String, title: String) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(stats, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        Text(title, style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileToolbar(backAction: () -> Unit, profile: ProfileDC?) {
    if (profile == null) return
    BoxWithConstraints(
        Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(profile.coverImage),
            modifier = Modifier
                .fillMaxWidth()
                .height(maxHeight * .20f),
            contentScale = ContentScale.Crop,
            contentDescription = "Cover Image"
        )
        TopAppBar(title = {}, navigationIcon = {
            IconButton(onClick = backAction) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent))

        Image(
            painter = painterResource(profile.avatar),
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 20.dp, y = 50.dp)
                .size(100.dp)
                .border(width = 5.dp, color = Color.White, shape = CircleShape)
                .clip(CircleShape)
        )
    }
}