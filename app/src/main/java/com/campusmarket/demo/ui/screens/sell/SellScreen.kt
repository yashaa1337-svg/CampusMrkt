@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.campusmarket.demo.ui.screens.sell

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.campusmarket.demo.data.mock.MockData
import com.campusmarket.demo.data.model.Category
import com.campusmarket.demo.data.model.Condition
import com.campusmarket.demo.data.model.Listing
import com.campusmarket.demo.ui.components.ListingCard
import com.campusmarket.demo.ui.theme.CampusMarketTheme

@Composable
fun SellScreen(
    meetupSpots: List<String>,
    onDemoAction: (String) -> Unit,
) {
    var title by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf<Category?>(null) }
    var condition by rememberSaveable { mutableStateOf(Condition.GOOD) }
    var price by rememberSaveable { mutableStateOf("") }
    var isFree by rememberSaveable { mutableStateOf(false) }
    var swapOk by rememberSaveable { mutableStateOf(false) }
    var courseCode by rememberSaveable { mutableStateOf("") }
    var meetupSpot by rememberSaveable { mutableStateOf<String?>(null) }
    var description by rememberSaveable { mutableStateOf("") }

    fun reset() {
        title = ""
        category = null
        condition = Condition.GOOD
        price = ""
        isFree = false
        swapOk = false
        courseCode = ""
        meetupSpot = null
        description = ""
    }

    // Live preview of what the form would publish.
    val previewListing = Listing(
        id = "preview",
        title = title.ifBlank { "Your item title" },
        description = description,
        priceEur = if (isFree) null else price.toIntOrNull() ?: 0,
        category = category ?: Category.ELECTRONICS,
        condition = condition,
        sellerId = MockData.me.id,
        campus = MockData.me.university,
        meetupSpot = meetupSpot ?: "Pick a spot",
        postedHoursAgo = 0,
        palette = category?.ordinal ?: 0,
        swapOk = swapOk,
        courseCode = courseCode.ifBlank { null },
    )

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                title = { Text("New listing") },
                actions = { TextButton(onClick = { reset() }) { Text("Clear") } },
            )
        },
        bottomBar = {
            Surface(tonalElevation = 3.dp, shadowElevation = 8.dp) {
                Button(
                    onClick = { onDemoAction("Demo: publishing isn't wired up – but the preview looks great") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .height(50.dp),
                ) {
                    Icon(imageVector = Icons.Outlined.Sell, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Publish listing")
                }
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            FormSection(title = "Photos", subtitle = "Up to 4 photos. The first one is the cover.") {
                PhotoSlots(onAdd = { onDemoAction("Demo: the camera stays off") })
            }

            FormSection(title = "Details") {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Title") },
                    placeholder = { Text("e.g. Calculus textbook, 8th ed.") },
                    singleLine = true,
                )
                CategoryPicker(selected = category, onSelect = { category = it })
                Text(
                    text = "Condition",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    Condition.entries.forEachIndexed { index, option ->
                        SegmentedButton(
                            selected = condition == option,
                            onClick = { condition = option },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = Condition.entries.size,
                            ),
                            label = { Text(option.label, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        )
                    }
                }
                AnimatedVisibility(visible = category == Category.TEXTBOOKS || category == Category.TUTORING) {
                    OutlinedTextField(
                        value = courseCode,
                        onValueChange = { courseCode = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Course / module code") },
                        placeholder = { Text("e.g. IN0007 or Econ 101") },
                        supportingText = { Text("Helps classmates from the same course find it") },
                        leadingIcon = { Icon(imageVector = Icons.Outlined.School, contentDescription = null) },
                        singleLine = true,
                    )
                }
            }

            FormSection(title = "Price") {
                OutlinedTextField(
                    value = price,
                    onValueChange = { new -> if (new.length <= 5 && new.all { it.isDigit() }) price = new },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Price") },
                    placeholder = { Text("0") },
                    prefix = { Text("€") },
                    enabled = !isFree,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = isFree,
                        onClick = { isFree = !isFree },
                        label = { Text("Give away for free") },
                        leadingIcon = { Text("🎁") },
                    )
                    FilterChip(
                        selected = swapOk,
                        onClick = { swapOk = !swapOk },
                        label = { Text("Open to swaps") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.SwapHoriz,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                            )
                        },
                    )
                }
            }

            FormSection(title = "Meet-up spot", subtitle = "Public campus spots only – that's the rule here.") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                ) {
                    meetupSpots.forEach { spot ->
                        val selected = meetupSpot == spot
                        val leading: (@Composable () -> Unit)? = if (selected) {
                            {
                                Icon(
                                    imageVector = Icons.Outlined.LocationOn,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                )
                            }
                        } else {
                            null
                        }
                        FilterChip(
                            selected = selected,
                            onClick = { meetupSpot = if (selected) null else spot },
                            label = { Text(spot) },
                            leadingIcon = leading,
                        )
                    }
                }
            }

            FormSection(title = "Description") {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Condition details, what's included, when you can hand it over…") },
                    minLines = 3,
                )
            }

            FormSection(title = "Preview", subtitle = "This is how your listing will look in the feed.") {
                ListingCard(
                    listing = previewListing,
                    isFavorite = false,
                    onClick = {},
                    onFavoriteClick = {},
                    modifier = Modifier.width(180.dp),
                )
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun FormSection(
    title: String,
    subtitle: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        content()
    }
}

@Composable
private fun PhotoSlots(onAdd: () -> Unit) {
    val outline = MaterialTheme.colorScheme.outline
    val shape = MaterialTheme.shapes.small
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        repeat(4) { index ->
            val isCover = index == 0
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(shape)
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    .clickable(onClick = onAdd)
                    .drawBehind {
                        drawRoundRect(
                            color = outline,
                            style = Stroke(
                                width = 1.5.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f)),
                            ),
                            cornerRadius = CornerRadius(12.dp.toPx()),
                        )
                    },
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = if (isCover) Icons.Outlined.AddAPhoto else Icons.Outlined.PhotoCamera,
                        contentDescription = if (isCover) "Add photo" else null,
                        tint = if (isCover) MaterialTheme.colorScheme.primary else outline,
                    )
                    if (isCover) {
                        Text(
                            text = "Add",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryPicker(selected: Category?, onSelect: (Category) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth()) {
        PickerField(
            label = "Category",
            value = selected?.let { "${it.emoji}  ${it.label}" },
            placeholder = "Choose a category",
            onClick = { expanded = true },
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Category.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    leadingIcon = { Text(option.emoji) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    },
                )
            }
        }
    }
}

/** Looks like an outlined text field but opens a menu instead of a keyboard. */
@Composable
private fun PickerField(
    label: String,
    value: String?,
    placeholder: String,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = value ?: placeholder,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (value == null) MaterialTheme.colorScheme.onSurfaceVariant
                    else MaterialTheme.colorScheme.onSurface,
                )
            }
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun SellScreenPreview() {
    CampusMarketTheme {
        SellScreen(meetupSpots = MockData.meetupSpots, onDemoAction = {})
    }
}
