package ru.esstu.student.profile.porfolio.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PortfolioFileRequestResponse(
    val id: Int? = null,//
    val type: PortfolioTypeResponse? = null,              // ACHIEVEMENT, CONFERENCE, AWARD, CONTEST, EXHIBITION, SCIENCEREPORT, WORK, TRAINEESHIP, REVIEWS, THEME, SCIENCEWORK,
    val studentCode: String? = null,      // код студента
    val eventName: String? = null,         //
    val eventStatus: String? = null,            //
    val eventPlace: String? = null,            //
    val eventStartDate: Long? = null,            //
    val eventEndDate: Long? = null,            //
    val eventUrl: String? = null,         //
    val workName: String? = null,         //
    val workType: String? = null,         //
    val result: String? = null,      //
    val coauthorsText: String? = null,               //
    val fileCode: String? = null,            // код файла
    val status: String? = null,                     // статус
)