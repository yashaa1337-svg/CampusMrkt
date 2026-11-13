package com.campusmarket.demo.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

object Routes {
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val SEARCH = "search"
    const val SELL = "sell"
    const val CHATS = "chats"
    const val PROFILE = "profile"

    const val ARG_LISTING_ID = "listingId"
    const val ARG_CHAT_ID = "chatId"
    const val LISTING = "listing/{$ARG_LISTING_ID}"
    const val CHAT = "chat/{$ARG_CHAT_ID}"

    fun listing(id: String) = "listing/$id"
    fun chat(id: String) = "chat/$id"
}

enum class TopLevelDestination(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    HOME(Routes.HOME, "Home", Icons.Filled.Home, Icons.Outlined.Home),
    SEARCH(Routes.SEARCH, "Search", Icons.Filled.Search, Icons.Outlined.Search),
    SELL(Routes.SELL, "Sell", Icons.Filled.AddCircle, Icons.Outlined.AddCircleOutline),
    CHATS(Routes.CHATS, "Chats", Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline),
    PROFILE(Routes.PROFILE, "Profile", Icons.Filled.Person, Icons.Outlined.Person),
}
