@file:OptIn(ExperimentalMaterial3Api::class)

package com.campusmarket.demo.ui.screens.chats

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.campusmarket.demo.data.mock.MockData
import com.campusmarket.demo.data.model.ChatRole
import com.campusmarket.demo.data.model.Conversation
import com.campusmarket.demo.data.model.Listing
import com.campusmarket.demo.data.model.User
import com.campusmarket.demo.ui.components.EmptyState
import com.campusmarket.demo.ui.components.ListingImage
import com.campusmarket.demo.ui.components.UserAvatar
import com.campusmarket.demo.ui.theme.CampusMarketTheme

@Composable
fun ChatsScreen(
    conversations: List<Conversation>,
    userById: (String) -> User?,
    listingById: (String) -> Listing?,
    onConversationClick: (Conversation) -> Unit,
    onDemoAction: (String) -> Unit,
) {
    var roleFilter by rememberSaveable { mutableStateOf<ChatRole?>(null) }
    val shown = remember(conversations, roleFilter) {
        roleFilter?.let { role -> conversations.filter { it.role == role } } ?: conversations
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chats") },
                actions = {
                    IconButton(onClick = { onDemoAction("Demo: chat settings aren't wired up") }) {
                        Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = "More")
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    FilterChip(
                        selected = roleFilter == null,
                        onClick = { roleFilter = null },
                        label = { Text("All") },
                    )
                    ChatRole.entries.forEach { role ->
                        FilterChip(
                            selected = roleFilter == role,
                            onClick = { roleFilter = if (roleFilter == role) null else role },
                            label = { Text(role.label) },
                        )
                    }
                }
            }
            if (shown.isEmpty()) {
                item {
                    EmptyState(
                        emoji = "💬",
                        title = "No chats yet",
                        body = "Message a seller from any listing and it shows up here.",
                    )
                }
            }
            items(shown, key = { it.id }) { conversation ->
                val user = userById(conversation.otherUserId)
                val listing = listingById(conversation.listingId)
                if (user != null && listing != null) {
                    ConversationRow(
                        conversation = conversation,
                        user = user,
                        listing = listing,
                        onClick = { onConversationClick(conversation) },
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 84.dp),
                        color = MaterialTheme.colorScheme.outlineVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun ConversationRow(
    conversation: Conversation,
    user: User,
    listing: Listing,
    onClick: () -> Unit,
) {
    val unread = conversation.unreadCount > 0
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box {
            UserAvatar(user = user, size = 52.dp)
            ListingImage(
                listing = listing,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(22.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                emojiSize = 10.sp,
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = conversation.lastActive,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (unread) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = "${conversation.role.label} · ${listing.title}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val last = conversation.lastMessage
                Text(
                    text = if (last.fromMe) "You: ${last.text}" else last.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (unread) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (unread) FontWeight.SemiBold else null,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                if (unread) {
                    Badge { Text("${conversation.unreadCount}") }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun ChatsScreenPreview() {
    CampusMarketTheme {
        ChatsScreen(
            conversations = MockData.conversations,
            userById = MockData::userById,
            listingById = MockData::listingById,
            onConversationClick = {},
            onDemoAction = {},
        )
    }
}
