package com.campusmarket.demo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.campusmarket.demo.data.mock.MockData
import com.campusmarket.demo.data.model.Listing
import com.campusmarket.demo.ui.theme.CampusMarketTheme
import com.campusmarket.demo.ui.theme.ListingPalettes

/**
 * Stand-in for a product photo: a gradient picked from the listing's palette plus the
 * category emoji. Keeps the demo fully offline and free of image assets.
 */
@Composable
fun ListingImage(
    listing: Listing,
    modifier: Modifier = Modifier,
    imageIndex: Int = 0,
    emojiSize: TextUnit = 40.sp,
) {
    val palette = ListingPalettes[(listing.palette + imageIndex) % ListingPalettes.size]
    Box(
        modifier = modifier
            .background(Brush.linearGradient(palette))
            .drawBehind {
                drawCircle(
                    color = Color.White.copy(alpha = 0.14f),
                    radius = size.minDimension * 0.55f,
                    center = Offset(size.width * 0.85f, size.height * 0.1f),
                )
                drawCircle(
                    color = Color.Black.copy(alpha = 0.08f),
                    radius = size.minDimension * 0.4f,
                    center = Offset(size.width * 0.1f, size.height * 0.95f),
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(text = listing.category.emoji, fontSize = emojiSize)
    }
}

@Preview
@Composable
private fun ListingImagePreview() {
    CampusMarketTheme {
        ListingImage(listing = MockData.listings[0], modifier = Modifier.size(160.dp))
    }
}
