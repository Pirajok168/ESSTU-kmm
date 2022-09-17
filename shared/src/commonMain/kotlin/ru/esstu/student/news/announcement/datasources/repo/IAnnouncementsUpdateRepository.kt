package ru.esstu.student.news.announcement.datasources.repo

import kotlinx.coroutines.flow.Flow
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.news.entities.NewsNode

interface IAnnouncementsUpdateRepository {
    fun getUpdates(): Flow<Response<List<NewsNode>>>
}