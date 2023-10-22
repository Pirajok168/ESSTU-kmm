package ru.esstu.news.announcement.domain


import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.news.announcement.domain.entities.NewsNode

interface IAnnouncementsRepository {
    suspend fun getAnnouncementsPage(offset: Int, limit: Int): Response<List<NewsNode>?>
    suspend fun clearCache()
}