package ru.esstu.android.firebase.domain.model.news

import java.util.*

data class FirebaseNewsResponse(
     val id: String,

     val title: String?,
     val body: String?,
     val time: Date
)