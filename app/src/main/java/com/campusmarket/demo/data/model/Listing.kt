package com.campusmarket.demo.data.model

data class Listing(
    val id: String,
    val title: String,
    val description: String,
    /** Price in euro; `null` means the item is given away for free. */
    val priceEur: Int?,
    val category: Category,
    val condition: Condition,
    val sellerId: String,
    val campus: String,
    val meetupSpot: String,
    val postedHoursAgo: Int,
    /** Index into the gradient palette that stands in for a product photo. */
    val palette: Int,
    val swapOk: Boolean = false,
    val courseCode: String? = null,
    val imageCount: Int = 3,
) {
    val isFree: Boolean get() = priceEur == null
}
