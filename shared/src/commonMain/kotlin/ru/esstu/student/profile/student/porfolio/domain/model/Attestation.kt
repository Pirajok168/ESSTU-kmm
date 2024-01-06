package ru.esstu.student.profile.student.porfolio.domain.model

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