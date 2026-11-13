package com.campusmarket.demo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.campusmarket.demo.data.mock.MockData
import com.campusmarket.demo.data.model.User
import com.campusmarket.demo.ui.theme.AvatarColors
import com.campusmarket.demo.ui.theme.CampusMarketTheme

/** Initials on a coloured circle; no photos in the demo. */
@Composable
fun UserAvatar(user: User, modifier: Modifier = Modifier, size: Dp = 40.dp) {
    val color = AvatarColors[user.avatarPalette % AvatarColors.size]
    val style = when {
        size >= 72.dp -> MaterialTheme.typography.headlineMedium
        size >= 48.dp -> MaterialTheme.typography.titleMedium
        else -> MaterialTheme.typography.labelMedium
    }
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = user.initials,
            color = Color.White,
            style = style,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview
@Composable
private fun UserAvatarPreview() {
    CampusMarketTheme {
        UserAvatar(user = MockData.me, size = 88.dp)
    }
}
