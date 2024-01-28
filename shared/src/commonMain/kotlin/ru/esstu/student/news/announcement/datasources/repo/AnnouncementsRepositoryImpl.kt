package ru.esstu.student.news.announcement.datasources.repo


import ru.esstu.domain.utill.workingDate.toInstant
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.doOnSuccess
import ru.esstu.student.news.announcement.datasources.api.NewsApi
import ru.esstu.student.news.announcement.datasources.toAnnouncements
import ru.esstu.student.news.announcement.datasources.toNewsWithAttachments
import ru.esstu.student.news.announcement.db.announcement.NewsDao
import ru.esstu.student.news.announcement.db.announcement.toNews
import ru.esstu.student.news.entities.NewsNode

class AnnouncementsRepositoryImpl(
    private val newsApi: NewsApi,
    private val newsDao: NewsDao,
) : IAnnouncementsRepository {
    override suspend fun getAnnouncementsPage(offset: Int, limit: Int): Response<List<NewsNode>> {
        val page = newsDao.getNewsWithAttachments(limit, offset).map { it.toNews() }
        if (page.isNotEmpty())
            return Response.Success(page)

        val response =
            newsApi.getAnnouncements(offset, limit).transform { it.toAnnouncements().sortedByDescending { it.date.toInstant().toEpochMilliseconds() } }


        return response
            .doOnSuccess {
                newsDao.setNewsWithAttachments(it.map { it.toNewsWithAttachments() })
            }
    }


    override suspend fun clearCache() {
        newsDao.clearAll()
    }
}