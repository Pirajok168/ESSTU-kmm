package ru.esstu.features.news.announcement.domain.repo


import ru.esstu.data.web.api.model.Response
import ru.esstu.data.web.api.model.doOnSuccess
import ru.esstu.domain.utill.workingDate.toInstant
import ru.esstu.features.news.announcement.data.api.NewsApi
import ru.esstu.features.news.announcement.data.db.NewsDao
import ru.esstu.features.news.announcement.data.db.toNews
import ru.esstu.features.news.announcement.domain.model.NewsNode
import ru.esstu.features.news.announcement.domain.toAnnouncements
import ru.esstu.features.news.announcement.domain.toNewsWithAttachments

class AnnouncementsRepositoryImpl(
    private val newsApi: NewsApi,
    private val newsDao: NewsDao,
) : IAnnouncementsRepository {
    override suspend fun getAnnouncementsPage(offset: Int, limit: Int): Response<List<NewsNode>> {
        val page = newsDao.getNewsWithAttachments(limit, offset).map { it.toNews() }
        if (page.isNotEmpty())
            return Response.Success(page)

        val response =
            newsApi.getAnnouncements(offset, limit).transform {
                it.toAnnouncements()
                    .sortedByDescending { it.date.toInstant().toEpochMilliseconds() }
            }


        return response
            .doOnSuccess {
                newsDao.setNewsWithAttachments(it.map { it.toNewsWithAttachments() })
            }
    }


    override suspend fun clearCache() {
        newsDao.clearAll()
    }
}