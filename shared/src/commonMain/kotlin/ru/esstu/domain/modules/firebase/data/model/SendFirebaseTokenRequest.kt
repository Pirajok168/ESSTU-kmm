package ru.esstu.domain.modules.firebase.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SendFirebaseTokenRequest (
    val token: String
)