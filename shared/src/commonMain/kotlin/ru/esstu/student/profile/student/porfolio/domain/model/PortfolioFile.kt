package ru.esstu.student.profile.student.porfolio.domain.model

import com.soywiz.klock.DateTime

sealed class PortfolioFile {

    abstract val id: Int?
    abstract val status: String
    abstract val title: String
    abstract val attachment:Attachment?

    data class Achievement(
        override val id:Int,
        override val status: String,
        override val title:String,
        override val attachment: Attachment?,
        val date: DateTime
    ): PortfolioFile()

    data class Award(
        override val id:Int,
        override val status: String,
        override val title:String,
        override val attachment: Attachment?,
        val date: DateTime
    ): PortfolioFile()
}