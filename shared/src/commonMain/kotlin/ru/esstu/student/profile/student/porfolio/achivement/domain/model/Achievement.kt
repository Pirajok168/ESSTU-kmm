package ru.esstu.student.profile.student.porfolio.achivement.domain.model

import com.soywiz.klock.DateTime

data class Achievement(
    val id:Int,
    val title:String,
    val status:String,
    val date: DateTime,
    val attachment: Attachment?
)
