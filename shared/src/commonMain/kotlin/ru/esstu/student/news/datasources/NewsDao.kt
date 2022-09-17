package ru.esstu.student.news.datasources

import ru.esstu.student.news.datasources.relations.NewsWithAttachments
import ru.esstu.student.news.entities.NewsNode

interface NewsDao {
    suspend fun getAllNewsWithAttachments(): List<NewsWithAttachments>

    suspend fun getNewsWithAttachments(limit: Int, offset: Int): List<NewsWithAttachments>

    suspend fun setNewsWithAttachments(news: List<NewsWithAttachments>)

    suspend fun clearAll()
}