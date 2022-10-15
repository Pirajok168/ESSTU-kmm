package ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.admission_info

data class AdmissionInfoResponse(
    val id: Int = 0,
    val entrantId: Int = 0,
    val levelOfEducation: String? = null,
    val direction: String? = null,          //id из справочника
    val admittanceCategory: String? = null,
    val budget: Boolean = false,            //выбрал бюджет
    val contract: Boolean = false,          //договор,
    val targetedTraining: Boolean = false,  //выбрал целевое,
    val quota: Boolean = false,             //выбрал льготы
    val consentBudget: Boolean = false,     //согласен на зачисление на бюджет
    val consentTarget: Boolean = false,     //согласен на зачисление на целевое
    val consentQuote: Boolean = false       //согласен на зачисление на льготное
)