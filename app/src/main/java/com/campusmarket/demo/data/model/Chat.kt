package com.campusmarket.demo.data.model

data class Message(
    val id: String,
    val fromMe: Boolean,
    val text: String,
    val time: String,
)

enum class ChatRole(val label: String) {
    BUYING("Buying"),
    SELLING("Selling"),
}

data class Conversation(
    val id: String,
    val otherUserId: String,
    val listingId: String,
    val role: ChatRole,
    val messages: List<Message>,
    val lastActive: String,
    val unreadCount: Int = 0,
) {
    val lastMessage: Message get() = messages.last()
}
