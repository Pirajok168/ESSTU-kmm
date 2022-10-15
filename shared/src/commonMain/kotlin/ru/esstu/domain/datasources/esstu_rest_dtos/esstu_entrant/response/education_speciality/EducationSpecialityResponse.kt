package ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.education_speciality

data class EducationSpecialityResponse(
    val specId: Long = 0,
    val eduFormId: Int = 0,
    val eduLevelId: Int = 0,
    val specialityName: String? = null,
    val specCode: String? = null,
    val numberBudget: Int = 0,
    val numberPaid: Int = 0,
    val numberTarget: Int = 0,
    val numberBenefit: Int = 0,
    val facultyId: Int = 0,
    val accelerated //Ускоренное обучение
    : Boolean? = null
)
