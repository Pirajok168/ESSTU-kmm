package ru.esstu.student.profile.student.porfolio.conference.data.model

import kotlinx.serialization.Serializable


@Serializable
data class ConferenceRequest (
    val eventName:String,
    val eventStatus:String,
    val eventPlace:String,
    val eventStartDate:Long,
    val eventEndDate:Long,
    val workName:String,
    val coauthorsText:String,
    val type:String = "CONFERENCE",
)