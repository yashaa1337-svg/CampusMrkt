package com.campusmarket.demo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.campusmarket.demo.data.model.Category
import com.campusmarket.demo.ui.theme.CampusMarketTheme

@Composable
fun CategoryChip(
    category: Category,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(category.label) },
        leadingIcon = { Text(category.emoji) },
        modifier = modifier,
    )
}

/** Horizontal, single-select row of all categories. Tapping the selected chip clears it. */
@Composable
fun CategoryChipRow(
    selected: Category?,
    onSelect: (Category?) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
) {
    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(Category.entries) { category ->
            CategoryChip(
                category = category,
                selected = category == selected,
                onClick = { onSelect(if (category == selected) null else category) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryChipRowPreview() {
    CampusMarketTheme {
        CategoryChipRow(selected = Category.TEXTBOOKS, onSelect = {})
    }
}
