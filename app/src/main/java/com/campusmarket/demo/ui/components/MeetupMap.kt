package com.campusmarket.demo.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.campusmarket.demo.ui.theme.CampusMarketTheme

/** A hand-drawn "map" placeholder: a few blocks, roads and a pin. No map SDK, no network. */
@Composable
fun MeetupMap(spot: String, modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    val background = scheme.surfaceContainerHigh
    val road = scheme.surfaceContainerLowest
    val block = scheme.surfaceContainerHighest
    val park = scheme.tertiaryContainer

    // Fractions of the canvas size, so the map scales with its container.
    val blocks = listOf(
        Rect(0.06f, 0.12f, 0.22f, 0.30f),
        Rect(0.06f, 0.40f, 0.18f, 0.52f),
        Rect(0.40f, 0.10f, 0.52f, 0.26f),
        Rect(0.42f, 0.72f, 0.60f, 0.92f),
        Rect(0.72f, 0.66f, 0.92f, 0.80f),
        Rect(0.08f, 0.74f, 0.26f, 0.90f),
    )

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(background),
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height

            drawRoundRect(
                color = park,
                topLeft = Offset(w * 0.58f, h * 0.08f),
                size = Size(w * 0.34f, h * 0.42f),
                cornerRadius = CornerRadius(20f, 20f),
            )
            blocks.forEach { r ->
                drawRoundRect(
                    color = block,
                    topLeft = Offset(w * r.left, h * r.top),
                    size = Size(w * r.width, h * r.height),
                    cornerRadius = CornerRadius(10f, 10f),
                )
            }

            val mainRoad = 12.dp.toPx()
            val sideRoad = 7.dp.toPx()
            drawLine(road, Offset(0f, h * 0.62f), Offset(w, h * 0.56f), strokeWidth = mainRoad, cap = StrokeCap.Round)
            drawLine(road, Offset(w * 0.30f, 0f), Offset(w * 0.34f, h), strokeWidth = mainRoad, cap = StrokeCap.Round)
            drawLine(road, Offset(w * 0.64f, h * 0.58f), Offset(w * 0.68f, h), strokeWidth = sideRoad, cap = StrokeCap.Round)
            drawLine(road, Offset(w * 0.32f, h * 0.34f), Offset(w * 0.56f, h * 0.30f), strokeWidth = sideRoad, cap = StrokeCap.Round)
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = null,
                tint = scheme.primary,
                modifier = Modifier.size(40.dp),
            )
            Surface(shape = CircleShape, color = scheme.surface, shadowElevation = 2.dp) {
                Text(
                    text = spot,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MeetupMapPreview() {
    CampusMarketTheme {
        MeetupMap(
            spot = "Main library entrance",
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(8.dp),
        )
    }
}
