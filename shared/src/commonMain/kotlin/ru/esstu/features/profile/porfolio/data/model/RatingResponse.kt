package ru.esstu.features.profile.porfolio.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RatingResponse(
    val subgroupRating: Int?, // рейтинг в подгруппе
    val studentsSubgroupCount: Int?, // количество студентов в подгруппе
    val groupRating: Int?, // рейтинг в группе
    val studentsGroupCount: Int?, // количество студентов в группе
    val journalRating: Int?, // рейтинг в потоке
    val studentsJournalCount: Int?, // количество студентов в потоке
    val attendance: Int?, // процент посещения
    val maxScore: Float?, // максибалл
    val currentScore: Float?, // текущая успеваемость (указаны в процентах от максибалла)
    val additionalControlWeight: Float?, // вес оценок за опросы
    val additionalScore: Float?, // оценка за опросы (указаны в процентах от максибалла)
    val intermediateWeight: Float?, // вес итогового КИ (ИКИ)
    val intermediateScore: Float?, // оценка за ИКИ (указаны в процентах от максибалла)
    val pollingScore: Float?, // оценка за опросы
    val pollingCount: Int, // количество опросов
    val intermediateGrade: String?, // Итоговая оценка

)
