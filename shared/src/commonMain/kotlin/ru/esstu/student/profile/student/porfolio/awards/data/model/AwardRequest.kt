package ru.esstu.student.profile.student.porfolio.awards.data.model

import kotlinx.serialization.Serializable


@Serializable
data class AwardRequest (
    val eventName:String,
    val eventStartDate:Long,
    val eventStatus:String,
    val type:String = "AWARD",
    val status:String = "PROJECT",
)