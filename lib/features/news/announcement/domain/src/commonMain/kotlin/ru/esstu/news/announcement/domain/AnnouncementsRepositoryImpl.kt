package ru.esstu.news.announcement.domain



import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.domain.ktor.CustomResponseException
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.news.announcement.domain.entities.NewsNode
import ru.esstu.news.announcement.domain.entities.toAnnouncements
import ru.esstu.student.news.announcement.datasources.toNewsWithAttachments
import ru.esstu.student.news.announcement.db.announcement.NewsDao
import ru.esstu.student.news.announcement.db.announcement.toNews
import ru.news.announcement.data.web.NewsApi


class AnnouncementsRepositoryImpl(
    private val auth: IAuthRepository,
    private val newsApi: NewsApi,
   // private val newsDao: NewsDao,
): IAnnouncementsRepository {
    override suspend fun getAnnouncementsPage(offset: Int, limit: Int): Response<List<NewsNode>?> {
       // val page = newsDao.getNewsWithAttachments(limit, offset).map { it.toNews() }
     //   if (page.isNotEmpty())
        //   return Response.Success(page)

        val response = auth.provideToken { token ->

            newsApi.getAnnouncements(token.access, offset, limit).data?.toAnnouncements()?.asReversed()
        }



        return response

    }


    override suspend fun clearCache() {
      //  newsDao.clearAll()
    }
}