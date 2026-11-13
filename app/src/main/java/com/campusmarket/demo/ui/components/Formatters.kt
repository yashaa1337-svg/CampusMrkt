package com.campusmarket.demo.ui.components

import java.util.Locale

fun formatPrice(priceEur: Int): String = "€$priceEur"

fun timeAgo(hoursAgo: Int): String = when {
    hoursAgo < 1 -> "Just now"
    hoursAgo < 24 -> "${hoursAgo}h ago"
    hoursAgo < 24 * 7 -> "${hoursAgo / 24}d ago"
    else -> "${hoursAgo / (24 * 7)}w ago"
}

fun formatRating(rating: Double): String = String.format(Locale.US, "%.1f", rating)
