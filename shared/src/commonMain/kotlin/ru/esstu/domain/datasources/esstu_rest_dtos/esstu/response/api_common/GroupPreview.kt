package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common

data class GroupPreview(
    val id: String,
    val name: String,
    val shortName: String?,
    val usersCount: Int,
    val hasSubgroups: Boolean,
    )
