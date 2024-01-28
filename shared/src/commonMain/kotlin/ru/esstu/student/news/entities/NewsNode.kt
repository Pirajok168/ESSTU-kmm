package ru.esstu.student.news.entities

import kotlinx.datetime.LocalDateTime


data class NewsNode(
    val id: Long,
    val from: Creator,
    val date: LocalDateTime,
    val title:String,
    val message: String,
    val attachments: List<AttachmentNews> = emptyList()
)