@file:OptIn(ExperimentalMaterial3Api::class)

package com.campusmarket.demo.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.campusmarket.demo.data.mock.MockData
import com.campusmarket.demo.data.model.Category
import com.campusmarket.demo.data.model.Listing
import com.campusmarket.demo.ui.components.CategoryChipRow
import com.campusmarket.demo.ui.components.EmptyState
import com.campusmarket.demo.ui.components.ListingCard
import com.campusmarket.demo.ui.components.SectionHeader
import com.campusmarket.demo.ui.theme.CampusMarketTheme

@Composable
fun HomeScreen(
    listings: List<Listing>,
    campuses: List<String>,
    isFavorite: (String) -> Boolean,
    onToggleFavorite: (String) -> Unit,
    onListingClick: (Listing) -> Unit,
    onSearchClick: () -> Unit,
    onDemoAction: (String) -> Unit,
) {
    var selectedCampus by rememberSaveable { mutableStateOf(campuses.first()) }
    var selectedCategory by rememberSaveable { mutableStateOf<Category?>(null) }

    val campusListings = remember(listings, selectedCampus) {
        listings.filter { it.campus == selectedCampus }.sortedBy { it.postedHoursAgo }
    }
    val freeListings = remember(campusListings) { campusListings.filter { it.isFree } }
    val feed = remember(campusListings, selectedCategory) {
        selectedCategory?.let { category -> campusListings.filter { it.category == category } }
            ?: campusListings
    }

    Scaffold(
        topBar = {
            HomeTopBar(
                selectedCampus = selectedCampus,
                campuses = campuses,
                onCampusChange = { selectedCampus = it },
                onNotificationsClick = { onDemoAction("Demo: notifications aren't wired up") },
            )
        },
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                SearchBarButton(onClick = onSearchClick)
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                CategoryChipRow(
                    selected = selectedCategory,
                    onSelect = { selectedCategory = it },
                    contentPadding = PaddingValues(0.dp),
                )
            }

            if (selectedCategory == null && freeListings.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SectionHeader(
                        title = "Free this week",
                        subtitle = "Grab it before someone else does",
                        actionLabel = "See all",
                        onAction = { selectedCategory = Category.FREE },
                    )
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(freeListings, key = { it.id }) { listing ->
                            ListingCard(
                                listing = listing,
                                isFavorite = isFavorite(listing.id),
                                onClick = { onListingClick(listing) },
                                onFavoriteClick = { onToggleFavorite(listing.id) },
                                modifier = Modifier.width(160.dp),
                            )
                        }
                    }
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                SectionHeader(
                    title = selectedCategory?.label ?: "Fresh on campus",
                    subtitle = "${feed.size} listings · $selectedCampus",
                )
            }

            if (feed.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    EmptyState(
                        emoji = "🕵️",
                        title = "Nothing here yet",
                        body = "Try another category or switch campus.",
                    )
                }
            } else {
                items(feed, key = { it.id }) { listing ->
                    ListingCard(
                        listing = listing,
                        isFavorite = isFavorite(listing.id),
                        onClick = { onListingClick(listing) },
                        onFavoriteClick = { onToggleFavorite(listing.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeTopBar(
    selectedCampus: String,
    campuses: List<String>,
    onCampusChange: (String) -> Unit,
    onNotificationsClick: () -> Unit,
) {
    TopAppBar(
        title = {
            CampusSelector(
                selected = selectedCampus,
                campuses = campuses,
                onChange = onCampusChange,
            )
        },
        actions = {
            IconButton(onClick = onNotificationsClick) {
                BadgedBox(badge = { Badge() }) {
                    Icon(imageVector = Icons.Outlined.Notifications, contentDescription = "Notifications")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
    )
}

@Composable
private fun CampusSelector(
    selected: String,
    campuses: List<String>,
    onChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        AssistChip(
            onClick = { expanded = true },
            label = { Text(selected, fontWeight = FontWeight.SemiBold) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.School,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
            },
            trailingIcon = {
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "Change campus")
            },
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            campuses.forEach { campus ->
                DropdownMenuItem(
                    text = { Text(campus) },
                    onClick = {
                        onChange(campus)
                        expanded = false
                    },
                    trailingIcon = {
                        if (campus == selected) {
                            Icon(imageVector = Icons.Outlined.Check, contentDescription = null)
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun SearchBarButton(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "Search textbooks, bikes, tickets…",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun HomeScreenPreview() {
    CampusMarketTheme {
        HomeScreen(
            listings = MockData.listings,
            campuses = MockData.campuses,
            isFavorite = { it in MockData.initialFavorites },
            onToggleFavorite = {},
            onListingClick = {},
            onSearchClick = {},
            onDemoAction = {},
        )
    }
}
