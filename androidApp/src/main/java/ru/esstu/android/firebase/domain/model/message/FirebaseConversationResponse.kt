package ru.esstu.android.firebase.domain.model.message

import java.util.*

data class FirebaseConversationResponse(
    val id: String,

    val messageId: String,
    val title: String?,
    val body: String?,
    val time: Date,

    val parentId: String,
    val attachmentsCount: Int,
    val fromUserId: String
)