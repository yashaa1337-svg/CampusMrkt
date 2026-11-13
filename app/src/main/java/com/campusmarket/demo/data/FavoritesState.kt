package com.campusmarket.demo.data

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf

/** In-memory favourites. Lives for the process lifetime only – this is a demo. */
@Stable
class FavoritesState(initial: Collection<String> = emptyList()) {
    private val ids = mutableStateListOf<String>().also { it.addAll(initial) }

    val all: List<String> get() = ids

    fun isFavorite(listingId: String): Boolean = listingId in ids

    fun toggle(listingId: String) {
        if (!ids.remove(listingId)) ids.add(listingId)
    }
}
