@file:OptIn(ExperimentalMaterial3Api::class)

package com.campusmarket.demo.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.campusmarket.demo.data.mock.MockData
import com.campusmarket.demo.data.model.Listing
import com.campusmarket.demo.data.model.User
import com.campusmarket.demo.ui.components.ListingCard
import com.campusmarket.demo.ui.components.ListingImage
import com.campusmarket.demo.ui.components.MeetupMap
import com.campusmarket.demo.ui.components.PriceTag
import com.campusmarket.demo.ui.components.SectionHeader
import com.campusmarket.demo.ui.components.UserAvatar
import com.campusmarket.demo.ui.components.VerifiedBadge
import com.campusmarket.demo.ui.components.formatRating
import com.campusmarket.demo.ui.components.timeAgo
import com.campusmarket.demo.ui.theme.Amber500
import com.campusmarket.demo.ui.theme.CampusMarketTheme

@Composable
fun ListingDetailScreen(
    listing: Listing,
    seller: User,
    similar: List<Listing>,
    isFavorite: (String) -> Boolean,
    onToggleFavorite: (String) -> Unit,
    onBack: () -> Unit,
    onListingClick: (Listing) -> Unit,
    onMessageSeller: () -> Unit,
    onDemoAction: (String) -> Unit,
) {
    Scaffold(
        // The hero image runs under the status bar; the bottom bar pads itself.
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            DetailActionBar(
                onOffer = { onDemoAction("Demo: offers aren't sent anywhere") },
                onMessage = onMessageSeller,
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding,
        ) {
            item {
                ImagePager(
                    listing = listing,
                    isFavorite = isFavorite(listing.id),
                    onBack = onBack,
                    onShare = { onDemoAction("Demo: sharing is disabled") },
                    onFavorite = { onToggleFavorite(listing.id) },
                )
            }
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    PriceTag(
                        priceEur = listing.priceEur,
                        swapOk = listing.swapOk,
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    Text(text = listing.title, style = MaterialTheme.typography.headlineSmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        InfoChip(text = listing.condition.label, icon = Icons.Outlined.CheckCircle)
                        InfoChip(text = listing.category.label, emoji = listing.category.emoji)
                        listing.courseCode?.let { InfoChip(text = it, icon = Icons.Outlined.School) }
                    }
                    Text(
                        text = "Posted ${timeAgo(listing.postedHoursAgo).lowercase()} · ${listing.campus}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    HorizontalDivider()
                    Text(text = "Description", style = MaterialTheme.typography.titleMedium)
                    Text(text = listing.description, style = MaterialTheme.typography.bodyLarge)

                    HorizontalDivider()
                    Text(text = "Seller", style = MaterialTheme.typography.titleMedium)
                    SellerCard(
                        seller = seller,
                        onClick = { onDemoAction("Demo: public profiles aren't wired up") },
                    )

                    HorizontalDivider()
                    Text(text = "Meet-up spot", style = MaterialTheme.typography.titleMedium)
                    MeetupMap(
                        spot = listing.meetupSpot,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(170.dp),
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp),
                        )
                        Text(
                            text = "${listing.meetupSpot} · ${listing.campus}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    SafetyTip()
                }
            }

            if (similar.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "More in ${listing.category.label}",
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                    Spacer(Modifier.height(8.dp))
                }
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(similar, key = { it.id }) { other ->
                            ListingCard(
                                listing = other,
                                isFavorite = isFavorite(other.id),
                                onClick = { onListingClick(other) },
                                onFavoriteClick = { onToggleFavorite(other.id) },
                                modifier = Modifier.width(160.dp),
                            )
                        }
                    }
                }
                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
private fun ImagePager(
    listing: Listing,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onShare: () -> Unit,
    onFavorite: () -> Unit,
) {
    val pagerState = rememberPagerState { listing.imageCount }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
    ) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            ListingImage(
                listing = listing,
                modifier = Modifier.fillMaxSize(),
                imageIndex = page,
                emojiSize = 120.sp,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            OverlayIconButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                onClick = onBack,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OverlayIconButton(
                    icon = Icons.Outlined.Share,
                    contentDescription = "Share",
                    onClick = onShare,
                )
                OverlayIconButton(
                    icon = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove from favourites" else "Add to favourites",
                    onClick = onFavorite,
                    tint = if (isFavorite) MaterialTheme.colorScheme.error else null,
                )
            }
        }

        if (listing.imageCount > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                repeat(listing.imageCount) { index ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == pagerState.currentPage) Color.White
                                else Color.White.copy(alpha = 0.5f),
                            ),
                    )
                }
            }
        }
    }
}

@Composable
private fun OverlayIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    tint: Color? = null,
) {
    FilledIconButton(
        onClick = onClick,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
            contentColor = tint ?: MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}

@Composable
private fun InfoChip(
    text: String,
    icon: ImageVector? = null,
    emoji: String? = null,
) {
    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surfaceContainerHigh) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp))
            }
            if (emoji != null) {
                Text(text = emoji, style = MaterialTheme.typography.labelMedium)
            }
            Text(text = text, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun SellerCard(seller: User, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            UserAvatar(user = seller, size = 48.dp)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(text = seller.name, style = MaterialTheme.typography.titleMedium)
                    if (seller.isVerified) VerifiedBadge(compact = true)
                }
                Text(
                    text = "${seller.faculty} · ${seller.year}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Amber500,
                        modifier = Modifier.size(14.dp),
                    )
                    Text(
                        text = "${formatRating(seller.rating)} (${seller.reviewCount})",
                        style = MaterialTheme.typography.labelMedium,
                    )
                    Text(
                        text = "· Replies ${seller.responseTime}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SafetyTip() {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(imageVector = Icons.Outlined.Shield, contentDescription = null)
            Text(
                text = "Stay safe: meet at a public campus spot, check the item before paying, and keep the chat in the app.",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun DetailActionBar(onOffer: () -> Unit, onMessage: () -> Unit) {
    Surface(tonalElevation = 3.dp, shadowElevation = 8.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedButton(
                onClick = onOffer,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
            ) {
                Text("Make an offer")
            }
            Button(
                onClick = onMessage,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text("Message seller")
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun ListingDetailScreenPreview() {
    val listing = MockData.listings[0]
    CampusMarketTheme {
        ListingDetailScreen(
            listing = listing,
            seller = MockData.userById(listing.sellerId)!!,
            similar = MockData.similarTo(listing),
            isFavorite = { true },
            onToggleFavorite = {},
            onBack = {},
            onListingClick = {},
            onMessageSeller = {},
            onDemoAction = {},
        )
    }
}
