package com.campusmarket.demo.ui.screens.search

import com.campusmarket.demo.data.model.Category
import com.campusmarket.demo.data.model.Condition
import com.campusmarket.demo.data.model.Listing

/** Filter state of the search screen. Plain data so it is trivial to preview and reset. */
data class SearchFilters(
    val category: Category? = null,
    val minPrice: Int = 0,
    val maxPrice: Int = MAX_PRICE,
    val conditions: Set<Condition> = emptySet(),
    val courseCode: String = "",
    val freeOnly: Boolean = false,
    val swapOnly: Boolean = false,
) {
    val isActive: Boolean get() = this != SearchFilters()

    private val hasPriceRange: Boolean get() = minPrice > 0 || maxPrice < MAX_PRICE

    val activeCount: Int
        get() = listOf(
            category != null,
            hasPriceRange,
            conditions.isNotEmpty(),
            courseCode.isNotBlank(),
            freeOnly,
            swapOnly,
        ).count { it }

    fun matches(listing: Listing): Boolean {
        if (category != null && listing.category != category) return false
        if (freeOnly && !listing.isFree) return false
        if (swapOnly && !listing.swapOk) return false
        if (conditions.isNotEmpty() && listing.condition !in conditions) return false
        if (courseCode.isNotBlank() &&
            listing.courseCode?.contains(courseCode.trim(), ignoreCase = true) != true
        ) return false
        val price = listing.priceEur ?: 0
        if (price < minPrice) return false
        if (maxPrice < MAX_PRICE && price > maxPrice) return false
        return true
    }

    fun priceLabel(): String =
        if (maxPrice >= MAX_PRICE) "€$minPrice+" else "€$minPrice – €$maxPrice"

    /** Chips shown above the results; each one knows how to remove itself. */
    fun activeChips(): List<ActiveFilter> = buildList {
        category?.let { c ->
            add(ActiveFilter("${c.emoji} ${c.label}") { copy(category = null) })
        }
        if (hasPriceRange) {
            add(ActiveFilter(priceLabel()) { copy(minPrice = 0, maxPrice = MAX_PRICE) })
        }
        conditions.forEach { c ->
            add(ActiveFilter(c.label) { copy(conditions = conditions - c) })
        }
        if (courseCode.isNotBlank()) {
            add(ActiveFilter("Course: ${courseCode.trim()}") { copy(courseCode = "") })
        }
        if (freeOnly) add(ActiveFilter("Free only") { copy(freeOnly = false) })
        if (swapOnly) add(ActiveFilter("Swap friendly") { copy(swapOnly = false) })
    }

    companion object {
        const val MAX_PRICE = 500
    }
}

class ActiveFilter(val label: String, val remove: () -> SearchFilters)

fun Listing.matchesQuery(query: String): Boolean {
    val q = query.trim()
    if (q.isEmpty()) return true
    return title.contains(q, ignoreCase = true) ||
        description.contains(q, ignoreCase = true) ||
        category.label.contains(q, ignoreCase = true) ||
        (courseCode?.contains(q, ignoreCase = true) == true)
}
