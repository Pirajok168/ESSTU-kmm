package ru.esstu.student.profile.student.porfolio.awards.domain.model

import com.soywiz.klock.DateTime
import ru.esstu.student.profile.student.porfolio.achivement.domain.model.Attachment

data class Award(
    val id:Int,
    val title:String,
    val status:String,
    val date: DateTime,
    val attachment: Attachment?
)
