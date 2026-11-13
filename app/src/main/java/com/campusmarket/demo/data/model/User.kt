package com.campusmarket.demo.data.model

data class User(
    val id: String,
    val name: String,
    val university: String,
    val faculty: String,
    val year: String,
    val isVerified: Boolean,
    val rating: Double,
    val reviewCount: Int,
    /** Index into the avatar colour list. */
    val avatarPalette: Int,
    val memberSince: String,
    val responseTime: String = "~1 h",
) {
    val initials: String
        get() = name.split(" ")
            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
            .take(2)
            .joinToString("")

    val firstName: String get() = name.substringBefore(' ')
}

data class Review(
    val id: String,
    val authorId: String,
    val rating: Int,
    val text: String,
    val daysAgo: Int,
)
