package ru.esstu.student.news.announcement.db.announcement.entities.relations

import ru.esstu.student.news.announcement.db.announcement.entities.NewsAttachmentEntity
import ru.esstu.student.news.announcement.db.announcement.entities.NewsEntity


data class NewsWithAttachments(
    val news: NewsEntity,
    val attachments: List<NewsAttachmentEntity>
)