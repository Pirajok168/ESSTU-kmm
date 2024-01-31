package ru.esstu.features.profile.porfolio.domain.model

import kotlinx.datetime.LocalDateTime


sealed class StudentPortfolioFile {

    abstract val id: Int?
    abstract val status: String?
    abstract val title: String
    abstract val attachment: Attachment?

    data class Achievement(
        override val id: Int,
        override val status: String,
        override val title: String,
        override val attachment: Attachment?,
        val date: LocalDateTime
    ) : StudentPortfolioFile()

    data class Award(
        override val id: Int,
        override val status: String,
        override val title: String,
        override val attachment: Attachment?,
        val date: LocalDateTime
    ) : StudentPortfolioFile()

    data class Conference(
        override val id: Int,
        override val title: String,
        override val status: String,
        override val attachment: Attachment?,

        val place: String,

        val theme: String,
        val coauthors: String,

        val startDate: LocalDateTime,
        val endDate: LocalDateTime,

        ) : StudentPortfolioFile()


    data class Contest(
        override val id: Int,
        override val title: String,
        override val status: String,
        override val attachment: Attachment?,

        val place: String,

        val result: String,

        val startDate: LocalDateTime,
        val endDate: LocalDateTime

    ) : StudentPortfolioFile()

    data class Exhibition(
        override val id: Int,
        override val title: String,
        override val attachment: Attachment?,
        override val status: String? = null,
        val place: String,

        val exhibit: String,

        val startDate: LocalDateTime,
        val endDate: LocalDateTime,

        ) : StudentPortfolioFile()


    data class ScienceReport(
        override val id: Int?,
        override val status: String?,
        override val title: String,
        override val attachment: Attachment?
    ) : StudentPortfolioFile()

    data class Work(
        override val id: Int?,
        override val status: String? = null,
        override val title: String,
        override val attachment: Attachment?,

        val type: String,
    ) : StudentPortfolioFile()


    data class Traineeship(
        override val id: Int?,
        override val status: String? = null,
        override val title: String,
        override val attachment: Attachment?,

        val place: String,
        val startDate: LocalDateTime,
        val endDate: LocalDateTime,
    ) : StudentPortfolioFile()

    data class Reviews(
        override val id: Int?,
        override val status: String? = null,
        override val title: String,
        override val attachment: Attachment?,

        val type: String,
    ) : StudentPortfolioFile()

    data class Theme(
        override val id: Int?,
        override val status: String? = null,
        override val title: String,
        override val attachment: Attachment?
    ) : StudentPortfolioFile()

    data class ScienceWorks(
        override val id: Int?,
        override val status: String? = null,
        override val title: String,
        override val attachment: Attachment?,

        val type: String,
        val coauthors: String,
    ) : StudentPortfolioFile()
}