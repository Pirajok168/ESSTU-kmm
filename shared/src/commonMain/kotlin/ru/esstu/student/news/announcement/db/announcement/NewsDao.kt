package ru.esstu.student.news.announcement.db.announcement

import ru.esstu.student.news.announcement.db.announcement.entities.relations.NewsWithAttachments

interface NewsDao {
    suspend fun getAllNewsWithAttachments(): List<NewsWithAttachments>

    suspend fun getNewsWithAttachments(limit: Int, offset: Int): List<NewsWithAttachments>

    suspend fun setNewsWithAttachments(news: List<NewsWithAttachments>)

    suspend fun clearAll()
}