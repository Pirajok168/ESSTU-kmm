package ru.esstu.features.news.announcement.data.db.entities.relations

import ru.esstu.features.news.announcement.data.db.entities.NewsAttachmentEntity
import ru.esstu.features.news.announcement.data.db.entities.NewsEntity


data class NewsWithAttachments(
    val news: NewsEntity,
    val attachments: List<NewsAttachmentEntity>
)