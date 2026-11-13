@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.campusmarket.demo.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.NorthWest
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.campusmarket.demo.data.mock.MockData
import com.campusmarket.demo.data.model.Category
import com.campusmarket.demo.data.model.Condition
import com.campusmarket.demo.data.model.Listing
import com.campusmarket.demo.ui.components.CategoryChip
import com.campusmarket.demo.ui.components.EmptyState
import com.campusmarket.demo.ui.components.ListingCard
import com.campusmarket.demo.ui.components.SectionHeader
import com.campusmarket.demo.ui.theme.CampusMarketTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SearchScreen(
    listings: List<Listing>,
    recentSearches: List<String>,
    popularSearches: List<String>,
    isFavorite: (String) -> Boolean,
    onToggleFavorite: (String) -> Unit,
    onListingClick: (Listing) -> Unit,
    onDemoAction: (String) -> Unit,
) {
    var query by rememberSaveable { mutableStateOf("") }
    var filters by remember { mutableStateOf(SearchFilters()) }
    var showFilters by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val showResults = query.isNotBlank() || filters.isActive
    val results = remember(listings, query, filters) {
        listings
            .filter { it.matchesQuery(query) && filters.matches(it) }
            .sortedBy { it.postedHoursAgo }
    }

    Scaffold(modifier = Modifier.imePadding()) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            SearchHeader(
                query = query,
                onQueryChange = { query = it },
                onClear = { query = "" },
                activeFilterCount = filters.activeCount,
                onFiltersClick = { showFilters = true },
                onSearch = { focusManager.clearFocus() },
            )
            if (showResults) {
                ResultsSection(
                    results = results,
                    filters = filters,
                    onFiltersChange = { filters = it },
                    isFavorite = isFavorite,
                    onToggleFavorite = onToggleFavorite,
                    onListingClick = onListingClick,
                )
            } else {
                SuggestionsSection(
                    recentSearches = recentSearches,
                    popularSearches = popularSearches,
                    onPick = { query = it },
                    onPickCategory = { filters = filters.copy(category = it) },
                    onClearHistory = { onDemoAction("Demo: search history isn't stored anyway") },
                )
            }
        }
    }

    if (showFilters) {
        FilterSheet(
            filters = filters,
            onFiltersChange = { filters = it },
            resultCount = results.size,
            onDismiss = { showFilters = false },
        )
    }
}

@Composable
private fun SearchHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    activeFilterCount: Int,
    onFiltersClick: () -> Unit,
    onSearch: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            placeholder = { Text("Search on campus") },
            leadingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = null) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = onClear) {
                        Icon(imageVector = Icons.Outlined.Close, contentDescription = "Clear search")
                    }
                }
            },
            shape = CircleShape,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        )
        BadgedBox(
            badge = {
                if (activeFilterCount > 0) Badge { Text("$activeFilterCount") }
            },
        ) {
            FilledTonalIconButton(onClick = onFiltersClick) {
                Icon(imageVector = Icons.Outlined.Tune, contentDescription = "Filters")
            }
        }
    }
}

@Composable
private fun ResultsSection(
    results: List<Listing>,
    filters: SearchFilters,
    onFiltersChange: (SearchFilters) -> Unit,
    isFavorite: (String) -> Boolean,
    onToggleFavorite: (String) -> Unit,
    onListingClick: (Listing) -> Unit,
) {
    val chips = filters.activeChips()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (chips.isNotEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    items(chips) { chip ->
                        InputChip(
                            selected = true,
                            onClick = { onFiltersChange(chip.remove()) },
                            label = { Text(chip.label) },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Close,
                                    contentDescription = "Remove filter",
                                    modifier = Modifier.size(18.dp),
                                )
                            },
                        )
                    }
                    item {
                        TextButton(onClick = { onFiltersChange(SearchFilters()) }) { Text("Clear all") }
                    }
                }
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = "${results.size} ${if (results.size == 1) "result" else "results"}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (results.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                EmptyState(
                    emoji = "🔍",
                    title = "No matches",
                    body = "Try a different word or loosen the filters.",
                )
            }
        } else {
            items(results, key = { it.id }) { listing ->
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

@Composable
private fun SuggestionsSection(
    recentSearches: List<String>,
    popularSearches: List<String>,
    onPick: (String) -> Unit,
    onPickCategory: (Category) -> Unit,
    onClearHistory: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        SectionHeader(title = "Recent searches", actionLabel = "Clear", onAction = onClearHistory)
        recentSearches.forEach { term ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
                    .clickable { onPick(term) }
                    .padding(vertical = 10.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.History,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = term,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f),
                )
                Icon(
                    imageVector = Icons.Outlined.NorthWest,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp),
                )
            }
        }

        Spacer(Modifier.height(24.dp))
        SectionHeader(title = "Popular on campus")
        Spacer(Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            popularSearches.forEach { term ->
                SuggestionChip(onClick = { onPick(term) }, label = { Text(term) })
            }
        }

        Spacer(Modifier.height(24.dp))
        SectionHeader(title = "Browse by category")
        Spacer(Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Category.entries.forEach { category ->
                AssistChip(
                    onClick = { onPickCategory(category) },
                    label = { Text(category.label) },
                    leadingIcon = { Text(category.emoji) },
                )
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun FilterSheet(
    filters: SearchFilters,
    onFiltersChange: (SearchFilters) -> Unit,
    resultCount: Int,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val close: () -> Unit = {
        scope.launch { sheetState.hide() }.invokeOnCompletion { onDismiss() }
    }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Filters",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f),
                )
                TextButton(onClick = { onFiltersChange(SearchFilters()) }) { Text("Reset") }
            }

            FilterGroup(title = "Category") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                ) {
                    Category.entries.forEach { category ->
                        CategoryChip(
                            category = category,
                            selected = filters.category == category,
                            onClick = {
                                onFiltersChange(
                                    filters.copy(category = if (filters.category == category) null else category),
                                )
                            },
                        )
                    }
                }
            }

            FilterGroup(title = "Price", trailing = filters.priceLabel()) {
                RangeSlider(
                    value = filters.minPrice.toFloat()..filters.maxPrice.toFloat(),
                    onValueChange = { range ->
                        onFiltersChange(
                            filters.copy(
                                minPrice = range.start.roundToInt(),
                                maxPrice = range.endInclusive.roundToInt(),
                            ),
                        )
                    },
                    valueRange = 0f..SearchFilters.MAX_PRICE.toFloat(),
                    steps = 49,
                )
            }

            FilterGroup(title = "Condition") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                ) {
                    Condition.entries.forEach { condition ->
                        val selected = condition in filters.conditions
                        FilterChip(
                            selected = selected,
                            onClick = {
                                onFiltersChange(
                                    filters.copy(
                                        conditions = if (selected) filters.conditions - condition
                                        else filters.conditions + condition,
                                    ),
                                )
                            },
                            label = { Text(condition.label) },
                        )
                    }
                }
            }

            FilterGroup(title = "Course code") {
                OutlinedTextField(
                    value = filters.courseCode,
                    onValueChange = { onFiltersChange(filters.copy(courseCode = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("e.g. IN0007 or Econ 101") },
                    leadingIcon = { Icon(imageVector = Icons.Outlined.School, contentDescription = null) },
                )
            }

            ToggleRow(
                title = "Free only",
                subtitle = "Only items given away for free",
                checked = filters.freeOnly,
                onCheckedChange = { onFiltersChange(filters.copy(freeOnly = it)) },
            )
            ToggleRow(
                title = "Swap friendly",
                subtitle = "Sellers open to trading instead of cash",
                checked = filters.swapOnly,
                onCheckedChange = { onFiltersChange(filters.copy(swapOnly = it)) },
            )

            Button(
                onClick = close,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
            ) {
                Text("Show $resultCount ${if (resultCount == 1) "listing" else "listings"}")
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun FilterGroup(
    title: String,
    trailing: String? = null,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
            )
            if (trailing != null) {
                Text(
                    text = trailing,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
        content()
    }
}

@Composable
private fun ToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
    CampusMarketTheme {
        SearchScreen(
            listings = MockData.listings,
            recentSearches = MockData.recentSearches,
            popularSearches = MockData.popularSearches,
            isFavorite = { it in MockData.initialFavorites },
            onToggleFavorite = {},
            onListingClick = {},
            onDemoAction = {},
        )
    }
}
