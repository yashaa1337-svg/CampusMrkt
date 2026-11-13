package com.campusmarket.demo.data.mock

import com.campusmarket.demo.data.model.Category
import com.campusmarket.demo.data.model.ChatRole
import com.campusmarket.demo.data.model.Condition
import com.campusmarket.demo.data.model.Conversation
import com.campusmarket.demo.data.model.Listing
import com.campusmarket.demo.data.model.Message
import com.campusmarket.demo.data.model.Review
import com.campusmarket.demo.data.model.User

/** Static demo content. Nothing here is persisted or fetched. */
object MockData {

    val campuses = listOf("TU Munich", "LMU Munich", "Hochschule München")

    val meetupSpots = listOf(
        "Main library entrance",
        "Mensa Arcisstraße",
        "Garching campus, MI building",
        "Olympiadorf",
        "Studentenstadt Freimann",
        "LMU main building",
    )

    val recentSearches = listOf("calculus", "desk lamp", "bike", "tollwood")
    val popularSearches = listOf("textbooks", "IKEA", "monitor", "tickets", "free", "bike")

    val initialFavorites = setOf("l3", "l5", "l8")

    val me = User(
        id = "u_me",
        name = "Lena Fischer",
        university = "TU Munich",
        faculty = "Informatics",
        year = "3rd year BSc",
        isVerified = true,
        rating = 4.9,
        reviewCount = 12,
        avatarPalette = 0,
        memberSince = "Sep 2024",
    )

    val users: List<User> = listOf(
        me,
        User(
            id = "u1", name = "Jonas Weber", university = "TU Munich",
            faculty = "Mechanical Engineering", year = "2nd year MSc",
            isVerified = true, rating = 4.8, reviewCount = 23, avatarPalette = 1,
            memberSince = "Oct 2023",
        ),
        User(
            id = "u2", name = "Aisha Rahman", university = "LMU Munich",
            faculty = "Economics", year = "1st year BSc",
            isVerified = true, rating = 5.0, reviewCount = 7, avatarPalette = 2,
            memberSince = "Oct 2025", responseTime = "~30 min",
        ),
        User(
            id = "u3", name = "Marco Rossi", university = "TU Munich",
            faculty = "Architecture", year = "4th year BSc",
            isVerified = true, rating = 4.6, reviewCount = 15, avatarPalette = 3,
            memberSince = "Apr 2023", responseTime = "~2 h",
        ),
        User(
            id = "u4", name = "Sofia Novak", university = "Hochschule München",
            faculty = "Design", year = "2nd year BSc",
            isVerified = true, rating = 4.9, reviewCount = 31, avatarPalette = 4,
            memberSince = "Mar 2024",
        ),
        User(
            id = "u5", name = "Tom Becker", university = "TU Munich",
            faculty = "Physics", year = "1st year MSc",
            isVerified = false, rating = 4.2, reviewCount = 3, avatarPalette = 5,
            memberSince = "Oct 2025", responseTime = "~1 day",
        ),
    )

    val listings: List<Listing> = listOf(
        Listing(
            id = "l1",
            title = "Calculus: Early Transcendentals (8th ed.)",
            description = "Stewart's Calculus, the edition used in Analysis 1. A few pencil notes in the first chapters, otherwise clean. Happy to swap for a linear algebra book.",
            priceEur = 25, category = Category.TEXTBOOKS, condition = Condition.GOOD,
            sellerId = "u1", campus = "TU Munich", meetupSpot = "Main library entrance",
            postedHoursAgo = 2, palette = 0, swapOk = true, courseCode = "MA0001 Analysis 1",
        ),
        Listing(
            id = "l2",
            title = "IKEA desk lamp, white",
            description = "Simple LED desk lamp, works perfectly. Moving out of the dorm so it has to go this week.",
            priceEur = 8, category = Category.DORM, condition = Condition.LIKE_NEW,
            sellerId = "u3", campus = "TU Munich", meetupSpot = "Mensa Arcisstraße",
            postedHoursAgo = 5, palette = 1,
        ),
        Listing(
            id = "l3",
            title = "City bike 28\", new brakes",
            description = "Reliable commuter bike, 7 gears, brakes replaced last month. Comes with a bell and a rear rack. Lock not included.",
            priceEur = 120, category = Category.BIKES, condition = Condition.GOOD,
            sellerId = "u1", campus = "TU Munich", meetupSpot = "Garching campus, MI building",
            postedHoursAgo = 26, palette = 2,
        ),
        Listing(
            id = "l4",
            title = "Free: moving boxes (x12)",
            description = "Twelve sturdy boxes from my move, folded flat. Just come and grab them.",
            priceEur = null, category = Category.FREE, condition = Condition.GOOD,
            sellerId = "u4", campus = "Hochschule München", meetupSpot = "Lothstraße entrance",
            postedHoursAgo = 3, palette = 3, imageCount = 2,
        ),
        Listing(
            id = "l5",
            title = "MacBook Air M1 13\" 256 GB",
            description = "Bought in 2022, battery health 89%. Small scratch on the lid, screen flawless. Charger included. Reset to factory settings before hand-over.",
            priceEur = 480, category = Category.ELECTRONICS, condition = Condition.GOOD,
            sellerId = "u2", campus = "LMU Munich", meetupSpot = "LMU main building",
            postedHoursAgo = 8, palette = 4, imageCount = 4,
        ),
        Listing(
            id = "l6",
            title = "Statistics tutoring (1 h sessions)",
            description = "Econ student with top grades in Stats 1 & 2. I explain the exam-relevant stuff without the fluff. First 20 minutes free.",
            priceEur = 15, category = Category.TUTORING, condition = Condition.NEW,
            sellerId = "u2", campus = "LMU Munich", meetupSpot = "LMU main building",
            postedHoursAgo = 12, palette = 5, courseCode = "Statistics 1", imageCount = 1,
        ),
        Listing(
            id = "l7",
            title = "Winter jacket, size M, barely worn",
            description = "Warm puffer jacket, dark green. Worn maybe five times, it's just too big for me.",
            priceEur = 30, category = Category.CLOTHING, condition = Condition.LIKE_NEW,
            sellerId = "u4", campus = "Hochschule München", meetupSpot = "Lothstraße entrance",
            postedHoursAgo = 20, palette = 6,
        ),
        Listing(
            id = "l8",
            title = "Tollwood festival tickets (2x), Saturday",
            description = "Two tickets for the Saturday evening concert, can't go anymore. Selling at face value, digital hand-over.",
            priceEur = 28, category = Category.TICKETS, condition = Condition.NEW,
            sellerId = "u3", campus = "TU Munich", meetupSpot = "Main library entrance",
            postedHoursAgo = 1, palette = 7, imageCount = 1,
        ),
        Listing(
            id = "l9",
            title = "Introduction to Algorithms (CLRS)",
            description = "The big one. Cover is worn, inside is fine. Swap for a Computer Architecture book welcome.",
            priceEur = 35, category = Category.TEXTBOOKS, condition = Condition.FAIR,
            sellerId = "u5", campus = "TU Munich", meetupSpot = "Garching campus, MI building",
            postedHoursAgo = 30, palette = 0, swapOk = true, courseCode = "IN0007 Algorithms",
        ),
        Listing(
            id = "l10",
            title = "Free: mini fridge (pickup only)",
            description = "Works, a bit loud. Needs to be gone by Sunday. Bring a friend, it's heavy.",
            priceEur = null, category = Category.FREE, condition = Condition.FAIR,
            sellerId = "u5", campus = "TU Munich", meetupSpot = "Studentenstadt Freimann",
            postedHoursAgo = 6, palette = 1, imageCount = 2,
        ),
        Listing(
            id = "l11",
            title = "Mechanical keyboard, TKL, brown switches",
            description = "Keychron K8, tactile browns, white backlight. Used for one semester, keycaps are clean.",
            priceEur = 45, category = Category.ELECTRONICS, condition = Condition.LIKE_NEW,
            sellerId = "u1", campus = "TU Munich", meetupSpot = "Garching campus, MI building",
            postedHoursAgo = 9, palette = 2,
        ),
        Listing(
            id = "l12",
            title = "Desk + chair set (dorm pickup)",
            description = "IKEA Linnmon desk with Flintan office chair. Minor scratches on the desk top. Pickup in Olympiadorf, 2nd floor, no lift.",
            priceEur = 40, category = Category.DORM, condition = Condition.GOOD,
            sellerId = "u3", campus = "TU Munich", meetupSpot = "Olympiadorf",
            postedHoursAgo = 48, palette = 3,
        ),
        Listing(
            id = "l13",
            title = "Scientific calculator TI-Nspire CX",
            description = "Approved for most engineering exams. Charger and cover included.",
            priceEur = 60, category = Category.ELECTRONICS, condition = Condition.GOOD,
            sellerId = "u2", campus = "LMU Munich", meetupSpot = "LMU main building",
            postedHoursAgo = 72, palette = 4, courseCode = "Engineering Math",
        ),
        Listing(
            id = "l14",
            title = "German A2–B1 textbook + workbook",
            description = "Menschen A2 and B1 with workbooks, no writing inside. Great for the uni language course.",
            priceEur = 12, category = Category.TEXTBOOKS, condition = Condition.GOOD,
            sellerId = "u4", campus = "Hochschule München", meetupSpot = "Lothstraße entrance",
            postedHoursAgo = 15, palette = 5, swapOk = true, courseCode = "German B1",
        ),
        Listing(
            id = "l15",
            title = "Uni hoodie, size L (swap for M welcome)",
            description = "Official TUM hoodie, navy. Washed twice. Would also swap for the same hoodie in M.",
            priceEur = 10, category = Category.CLOTHING, condition = Condition.GOOD,
            sellerId = "u5", campus = "TU Munich", meetupSpot = "Main library entrance",
            postedHoursAgo = 4, palette = 6, swapOk = true,
        ),
        Listing(
            id = "l16",
            title = "Jazz night ticket, Unterfahrt, Thu",
            description = "One ticket for Thursday's jazz session. Exam got moved, so I can't make it.",
            priceEur = 18, category = Category.TICKETS, condition = Condition.NEW,
            sellerId = "u2", campus = "LMU Munich", meetupSpot = "LMU main building",
            postedHoursAgo = 36, palette = 7, imageCount = 1,
        ),
        Listing(
            id = "l17",
            title = "Python tutoring for beginners",
            description = "Struggling with the intro programming course? I tutor 1:1, bring your assignment and we work through it together.",
            priceEur = 20, category = Category.TUTORING, condition = Condition.NEW,
            sellerId = "u_me", campus = "TU Munich", meetupSpot = "Garching campus, MI building",
            postedHoursAgo = 50, palette = 0, courseCode = "IN0001 Intro to Programming", imageCount = 1,
        ),
        Listing(
            id = "l18",
            title = "Monitor 24\" Dell, HDMI + DP",
            description = "Dell P2419H, Full HD, height-adjustable stand. Both cables included. No dead pixels.",
            priceEur = 70, category = Category.ELECTRONICS, condition = Condition.GOOD,
            sellerId = "u_me", campus = "TU Munich", meetupSpot = "Main library entrance",
            postedHoursAgo = 72, palette = 2,
        ),
        Listing(
            id = "l19",
            title = "Free: houseplants (3 pothos)",
            description = "Three healthy pothos cuttings in small pots. Going abroad next semester and can't take them.",
            priceEur = null, category = Category.FREE, condition = Condition.NEW,
            sellerId = "u3", campus = "TU Munich", meetupSpot = "Olympiadorf",
            postedHoursAgo = 10, palette = 3, imageCount = 2,
        ),
        Listing(
            id = "l20",
            title = "Economics year 1 bundle (Mankiw + notes)",
            description = "Principles of Economics plus my own summarised lecture notes for Micro and Macro. Passed both with a 1.3.",
            priceEur = 22, category = Category.TEXTBOOKS, condition = Condition.GOOD,
            sellerId = "u2", campus = "LMU Munich", meetupSpot = "LMU main building",
            postedHoursAgo = 19, palette = 1, swapOk = true, courseCode = "Econ 101",
        ),
        Listing(
            id = "l21",
            title = "Bike lock + lights set",
            description = "Abus U-lock and a USB-rechargeable front/rear light set. Sold my bike, don't need them anymore.",
            priceEur = 15, category = Category.BIKES, condition = Condition.LIKE_NEW,
            sellerId = "u_me", campus = "TU Munich", meetupSpot = "Garching campus, MI building",
            postedHoursAgo = 96, palette = 5,
        ),
    )

    val conversations: List<Conversation> = listOf(
        Conversation(
            id = "c1", otherUserId = "u1", listingId = "l1", role = ChatRole.BUYING,
            lastActive = "12:40", unreadCount = 1,
            messages = listOf(
                Message("m1", fromMe = true, text = "Hi Jonas! Is the calculus book still available?", time = "12:02"),
                Message("m2", fromMe = false, text = "Yes! A few pencil notes in chapter 1–3, but all pages are intact.", time = "12:15"),
                Message("m3", fromMe = true, text = "Great. Would you do 20?", time = "12:31"),
                Message("m4", fromMe = false, text = "22 and we have a deal 🙂 Library entrance tomorrow at 12?", time = "12:40"),
            ),
        ),
        Conversation(
            id = "c3", otherUserId = "u2", listingId = "l18", role = ChatRole.SELLING,
            lastActive = "09:18", unreadCount = 2,
            messages = listOf(
                Message("m1", fromMe = false, text = "Hey Lena, does the monitor come with a HDMI cable?", time = "08:55"),
                Message("m2", fromMe = true, text = "Yes, both HDMI and DisplayPort cables are included.", time = "09:04"),
                Message("m3", fromMe = false, text = "Perfect! I could pick it up on Friday after my 2pm lecture.", time = "09:17"),
                Message("m4", fromMe = false, text = "Library entrance works for me if that's fine with you.", time = "09:18"),
            ),
        ),
        Conversation(
            id = "c5", otherUserId = "u5", listingId = "l17", role = ChatRole.SELLING,
            lastActive = "Yesterday", unreadCount = 1,
            messages = listOf(
                Message("m1", fromMe = false, text = "Hi Lena, I'm stuck on the IN0001 assignments. Do you do 1:1 sessions?", time = "18:20"),
                Message("m2", fromMe = true, text = "Sure, €20/h. We can meet in the MI building, there are quiet rooms on the first floor.", time = "18:45"),
                Message("m3", fromMe = false, text = "Sounds good, does Tuesday 4pm work?", time = "19:02"),
            ),
        ),
        Conversation(
            id = "c2", otherUserId = "u3", listingId = "l12", role = ChatRole.BUYING,
            lastActive = "Yesterday",
            messages = listOf(
                Message("m1", fromMe = true, text = "Hi! Is the desk still available? Does the chair have armrests?", time = "14:10"),
                Message("m2", fromMe = false, text = "Still here. The chair has armrests, yes. Pickup only though, 2nd floor and no lift 😅", time = "15:32"),
                Message("m3", fromMe = true, text = "No problem, I'll bring a friend. Saturday morning?", time = "15:40"),
            ),
        ),
        Conversation(
            id = "c4", otherUserId = "u4", listingId = "l4", role = ChatRole.BUYING,
            lastActive = "Mon",
            messages = listOf(
                Message("m1", fromMe = true, text = "Hey Sofia, could I take 6 of the boxes?", time = "10:05"),
                Message("m2", fromMe = false, text = "Of course! They're right by the Lothstraße entrance, just grab them 👍", time = "10:20"),
                Message("m3", fromMe = true, text = "Got them, thanks a lot!!", time = "16:48"),
            ),
        ),
    )

    val reviewsForMe: List<Review> = listOf(
        Review("r1", authorId = "u1", rating = 5, text = "Keyboard was exactly as described and Lena was on time. Smooth hand-over at the library.", daysAgo = 3),
        Review("r2", authorId = "u4", rating = 5, text = "Super friendly, replied within minutes. Would buy from again!", daysAgo = 12),
        Review("r3", authorId = "u3", rating = 4, text = "Good tutoring session, explained recursion better than the lecture did.", daysAgo = 27),
        Review("r4", authorId = "u2", rating = 5, text = "Quick, easy, fair price. Thanks!", daysAgo = 41),
    )

    fun userById(id: String): User? = users.firstOrNull { it.id == id }

    fun listingById(id: String): Listing? = listings.firstOrNull { it.id == id }

    fun conversationById(id: String): Conversation? = conversations.firstOrNull { it.id == id }

    fun conversationForListing(listingId: String): Conversation? =
        conversations.firstOrNull { it.listingId == listingId }

    fun similarTo(listing: Listing): List<Listing> =
        listings.filter { it.category == listing.category && it.id != listing.id }.take(6)

    fun myListings(): List<Listing> = listings.filter { it.sellerId == me.id }

    val unreadChats: Int get() = conversations.count { it.unreadCount > 0 }
}
