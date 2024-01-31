package ru.esstu.features.profile.porfolio.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SubjectResponse(
    val id: Int?,
    val code: String?,
    val complexityTotal: Int?,
    val complexityZet: String?,
    val name: String?,
    val shortName: String?,
    val type: String?
)