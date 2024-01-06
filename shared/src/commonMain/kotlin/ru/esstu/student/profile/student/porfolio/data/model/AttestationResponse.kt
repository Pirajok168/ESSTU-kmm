package ru.esstu.student.profile.student.porfolio.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AttestationResponse(
    val journalId: String?,
    val code: String?,
    @SerialName("ayear")
    val year: String?,
    val rating: RatingResponse?,
    val college: Boolean?,
    val controlType: ControlTypeResponse?,
    val countMarks: String?, //балл?ы
    val courseActivity: String?,
    val eduBlock: Int?,
    val eduYear: Int?,
    val facultative: Boolean?,
    val nameMarks: String?, //оценка
    val note: String?,
    val subject: SubjectResponse?,
    val summaryGrade: String?
)
