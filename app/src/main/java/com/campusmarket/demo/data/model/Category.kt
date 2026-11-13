package com.campusmarket.demo.data.model

/** Listing categories tailored to student life. The emoji doubles as the category illustration. */
enum class Category(val label: String, val emoji: String) {
    TEXTBOOKS("Textbooks", "📚"),
    ELECTRONICS("Electronics", "💻"),
    DORM("Dorm & Furniture", "🛋️"),
    BIKES("Bikes", "🚲"),
    CLOTHING("Clothing", "👕"),
    TICKETS("Tickets & Events", "🎟️"),
    TUTORING("Tutoring & Services", "🎓"),
    FREE("Free stuff", "🎁"),
}

enum class Condition(val label: String) {
    NEW("New"),
    LIKE_NEW("Like new"),
    GOOD("Good"),
    FAIR("Fair"),
}
