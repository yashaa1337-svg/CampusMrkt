@file:OptIn(ExperimentalMaterial3Api::class)

package com.campusmarket.demo.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.campusmarket.demo.data.mock.MockData
import com.campusmarket.demo.data.model.Listing
import com.campusmarket.demo.data.model.Review
import com.campusmarket.demo.data.model.User
import com.campusmarket.demo.ui.components.EmptyState
import com.campusmarket.demo.ui.components.ListingCard
import com.campusmarket.demo.ui.components.UserAvatar
import com.campusmarket.demo.ui.components.VerifiedBadge
import com.campusmarket.demo.ui.components.formatRating
import com.campusmarket.demo.ui.theme.Amber500
import com.campusmarket.demo.ui.theme.CampusMarketTheme

@Composable
fun ProfileScreen(
    user: User,
    myListings: List<Listing>,
    favoriteListings: List<Listing>,
    reviews: List<Review>,
    userById: (String) -> User?,
    isFavorite: (String) -> Boolean,
    onToggleFavorite: (String) -> Unit,
    onListingClick: (Listing) -> Unit,
    onDemoAction: (String) -> Unit,
) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf(
        "Listings (${myListings.size})",
        "Favourites (${favoriteListings.size})",
        "Reviews (${reviews.size})",
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                actions = {
                    IconButton(onClick = { onDemoAction("Demo: settings aren't wired up") }) {
                        Icon(imageVector = Icons.Outlined.Settings, contentDescription = "Settings")
                    }
                },
            )
        },
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                ProfileHeader(
                    user = user,
                    activeListings = myListings.size,
                    onEdit = { onDemoAction("Demo: profile editing isn't wired up") },
                    onShare = { onDemoAction("Demo: sharing is disabled") },
                )
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                PrimaryTabRow(selectedTabIndex = tab) {
                    tabs.forEachIndexed { index, label ->
                        Tab(
                            selected = tab == index,
                            onClick = { tab = index },
                            text = { Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        )
                    }
                }
            }

            when (tab) {
                0 -> if (myListings.isEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        EmptyState(
                            emoji = "📦",
                            title = "No listings yet",
                            body = "Tap Sell to post your first item.",
                        )
                    }
                } else {
                    items(myListings, key = { "mine-${it.id}" }) { listing ->
                        ListingCard(
                            listing = listing,
                            isFavorite = isFavorite(listing.id),
                            onClick = { onListingClick(listing) },
                            onFavoriteClick = { onToggleFavorite(listing.id) },
                        )
                    }
                }

                1 -> if (favoriteListings.isEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        EmptyState(
                            emoji = "💜",
                            title = "Nothing saved yet",
                            body = "Tap the heart on a listing to keep it here.",
                        )
                    }
                } else {
                    items(favoriteListings, key = { "fav-${it.id}" }) { listing ->
                        ListingCard(
                            listing = listing,
                            isFavorite = isFavorite(listing.id),
                            onClick = { onListingClick(listing) },
                            onFavoriteClick = { onToggleFavorite(listing.id) },
                        )
                    }
                }

                else -> if (reviews.isEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        EmptyState(
                            emoji = "⭐",
                            title = "No reviews yet",
                            body = "Reviews show up after your first hand-over.",
                        )
                    }
                } else {
                    items(
                        items = reviews,
                        key = { it.id },
                        span = { GridItemSpan(maxLineSpan) },
                    ) { review ->
                        ReviewRow(review = review, author = userById(review.authorId))
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    user: User,
    activeListings: Int,
    onEdit: () -> Unit,
    onShare: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        UserAvatar(user = user, size = 88.dp)
        Text(text = user.name, style = MaterialTheme.typography.headlineSmall)
        Text(
            text = "${user.university} · ${user.faculty}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = user.year,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (user.isVerified) VerifiedBadge()

        Spacer(Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Stat(value = "${user.reviewCount}", label = "Deals")
            Stat(value = "$activeListings", label = "Active")
            Stat(value = formatRating(user.rating), label = "Rating", star = true)
        }
        Spacer(Modifier.height(4.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onEdit, modifier = Modifier.weight(1f)) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(6.dp))
                Text("Edit profile")
            }
            OutlinedButton(onClick = onShare, modifier = Modifier.weight(1f)) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(6.dp))
                Text("Share")
            }
        }
        Text(
            text = "Member since ${user.memberSince}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun Stat(value: String, label: String, star: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            if (star) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = Amber500,
                    modifier = Modifier.size(16.dp),
                )
            }
            Text(text = value, style = MaterialTheme.typography.titleLarge)
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ReviewRow(review: Review, author: User?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (author != null) UserAvatar(user = author, size = 40.dp)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = author?.name ?: "Student",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = "${review.daysAgo}d ago",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            StarRating(rating = review.rating)
            Text(text = review.text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun StarRating(rating: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = if (index < rating) Amber500 else MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun ProfileScreenPreview() {
    CampusMarketTheme {
        ProfileScreen(
            user = MockData.me,
            myListings = MockData.myListings(),
            favoriteListings = MockData.initialFavorites.mapNotNull(MockData::listingById),
            reviews = MockData.reviewsForMe,
            userById = MockData::userById,
            isFavorite = { it in MockData.initialFavorites },
            onToggleFavorite = {},
            onListingClick = {},
            onDemoAction = {},
        )
    }
}
