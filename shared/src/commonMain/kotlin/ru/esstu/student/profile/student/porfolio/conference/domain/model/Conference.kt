package ru.esstu.student.profile.student.porfolio.conference.domain.model

import com.soywiz.klock.DateTime
import ru.esstu.student.profile.student.porfolio.achivement.domain.model.Attachment
data class Conference(
    val id:Int,
    val title:String,
    val status:String,

    val place:String,

    val theme:String,
    val coauthors:String,

    val startDate:DateTime,
    val endDate:DateTime,

    val attachment:Attachment?
)
