package ru.esstu.student.profile.porfolio.data.model

import kotlinx.serialization.Serializable


@Serializable
enum class PortfolioTypeResponse {
    ACHIEVEMENT,
    CONFERENCE,
    AWARD,
    CONTEST,
    EXHIBITION,
    SCIENCEREPORT,
    WORK,
    TRAINEESHIP,
    REVIEWS,
    THEME,
    SCIENCEWORK
}