package ru.esstu.student.news.announcement.datasources.repo

import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.news.entities.NewsNode

interface IAnnouncementsRepository {
    suspend fun getAnnouncementsPage(offset: Int, limit: Int): Response<List<NewsNode>>
    suspend fun clearCache()
}