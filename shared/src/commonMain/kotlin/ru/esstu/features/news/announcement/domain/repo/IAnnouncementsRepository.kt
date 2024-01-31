package ru.esstu.features.news.announcement.domain.repo

import ru.esstu.data.web.api.model.Response
import ru.esstu.features.news.announcement.domain.model.NewsNode

interface IAnnouncementsRepository {
    suspend fun getAnnouncementsPage(offset: Int, limit: Int): Response<List<NewsNode>>
    suspend fun clearCache()
}