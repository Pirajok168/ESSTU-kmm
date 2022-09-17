package ru.esstu.student.news.announcement.datasources.repo

import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.news.announcement.datasources.api.NewsApi
import ru.esstu.student.news.announcement.datasources.toAnnouncements
import ru.esstu.student.news.announcement.datasources.toNewsWithAttachments
import ru.esstu.student.news.datasources.NewsDao
import ru.esstu.student.news.datasources.toNews
import ru.esstu.student.news.entities.NewsNode

class AnnouncementsRepositoryImpl(
    private val auth: IAuthRepository,
    private val newsApi: NewsApi,
    private val newsDao: NewsDao,
): IAnnouncementsRepository {
    override suspend fun getAnnouncementsPage(offset: Int, limit: Int): Response<List<NewsNode>> {
        val page = newsDao.getNewsWithAttachments(limit, offset).map { it.toNews() }
        if (page.isNotEmpty())
           return Response.Success(page)

        val response = auth.provideToken { type, token ->
            newsApi.getAnnouncements("$token", offset, limit).toAnnouncements().asReversed()
        }

        return when (response) {
            is Response.Error -> response
            is Response.Success -> {
                newsDao.setNewsWithAttachments(response.data.map { it.toNewsWithAttachments() })
                response
            }
        }
    }

    override suspend fun clearCache() {
        newsDao.clearAll()
    }
}