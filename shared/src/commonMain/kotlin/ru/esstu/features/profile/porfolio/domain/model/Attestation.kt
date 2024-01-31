package ru.esstu.features.profile.porfolio.domain.model

data class Attestation(
    val year: String?,
    val eduBlock: Int?,
    val eduYear: Int?,
    val nameMarks: String?,
    val summaryGrade: String?,
    val countMarks: String?,
    val controlType: ControlType?,
    val subject: Subject?
)