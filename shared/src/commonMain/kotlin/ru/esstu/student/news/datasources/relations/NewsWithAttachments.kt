package ru.esstu.student.news.datasources.relations

import ru.esstu.student.news.datasources.NewsAttachmentEntity
import ru.esstu.student.news.datasources.NewsEntity


data class NewsWithAttachments(
    val news: NewsEntity,
    val attachments: List<NewsAttachmentEntity>
)