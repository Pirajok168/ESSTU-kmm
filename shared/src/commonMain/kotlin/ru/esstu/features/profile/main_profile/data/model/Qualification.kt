package ru.esstu.features.profile.main_profile.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Qualification(
    @SerialName("id")
    val id: Int?,
    @SerialName("name")
    val name: String?
)