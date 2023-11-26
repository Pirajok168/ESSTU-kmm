package ru.esstu.student.profile.student.porfolio.achivement.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AchievementRequest (
    val eventName:String,
    val eventStartDate:Long,
    val eventStatus:String,
    val type:String = "ACHIEVEMENT",
    val status:String = "PROJECT",
)