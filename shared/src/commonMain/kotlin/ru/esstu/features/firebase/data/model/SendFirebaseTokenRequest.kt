package ru.esstu.features.firebase.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SendFirebaseTokenRequest(
    val token: String
)