package ru.esstu.android.firebase.domain.model.message

import java.util.*

data class FirebaseDialogResponse(
     val id: String,

     val messageId: String,
     val title: String?,
     val body: String?,
     val time: Date,

     val from: String,
     val attachmentsCount: Int,
)