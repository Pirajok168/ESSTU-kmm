package ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.personal_data

import java.util.*


data class PersonalData(
    val id: Int,
    val entrantId: Int,
    val dateOfBirth: Long?,
    val cityOfBirth: String?,
    val familyName: String?,
    val gender: String?,
    val name: String?,
    val patronymic: String?,
    val regionOfBirth: String?,
    val snills: String?
)