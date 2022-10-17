package ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.questionnaire



data class QuestionnaireResponse (
    val id: Int = 0,
    val entrantId: Int = 0,
    val name: String? = null,
    val familyName: String? = null,
    val patronymic: String? = null,
    val levelOfEducation: String? = null,
    val dateOfCreation: Long? = null,
    val dateOfSend: Long? = null,
    val dateOfAnswer: Long? = null,
    val status: String? = null,
    val contract: String? = null,
    val accelerated: Boolean? = null,
    val email: String? = null,
    val mobileNumber: String? = null
)