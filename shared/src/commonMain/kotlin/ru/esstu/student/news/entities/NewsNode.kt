package ru.esstu.student.news.entities

import com.soywiz.klock.DateTime


data class NewsNode(
    val id: Long,
    val from: Creator,
    val date: DateTime,
    val title:String,
    val message: String,
    val attachments: List<AttachmentNews> = emptyList()
)