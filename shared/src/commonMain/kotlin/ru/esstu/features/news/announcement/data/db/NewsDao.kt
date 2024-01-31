package ru.esstu.features.news.announcement.data.db

import ru.esstu.features.news.announcement.data.db.entities.relations.NewsWithAttachments

interface NewsDao {
    suspend fun getAllNewsWithAttachments(): List<NewsWithAttachments>

    suspend fun getNewsWithAttachments(limit: Int, offset: Int): List<NewsWithAttachments>

    suspend fun setNewsWithAttachments(news: List<NewsWithAttachments>)

    suspend fun clearAll()
}