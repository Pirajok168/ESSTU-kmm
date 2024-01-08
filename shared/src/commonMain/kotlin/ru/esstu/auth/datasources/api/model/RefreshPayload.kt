package ru.esstu.auth.datasources.api.model

import kotlinx.serialization.Serializable

@Serializable
data class RefreshPayload(
    val refresh_token: String,
    val response_type: String = "token",
    val grant_type: String = "refresh_token",
    val scope: String = "scope",
    val client_id: String = "personal_office_employee",
    val client_secret: String = "0a36339cfb8136da2151301170a730d758a88c0f130bd15fc1abe583a91ccfae"
)
