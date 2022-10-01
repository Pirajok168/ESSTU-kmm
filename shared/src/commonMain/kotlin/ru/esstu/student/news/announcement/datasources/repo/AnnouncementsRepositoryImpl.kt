package ru.esstu.student.news.announcement.datasources.repo

import io.github.aakira.napier.Napier
import io.ktor.client.plugins.*
import io.ktor.utils.io.errors.*
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.domain.ktor.CustomResponseException
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
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

        val response = auth.provideToken { token ->

            newsApi.getAnnouncements(token.access, offset, limit).toAnnouncements().asReversed()

        }



        return try {
            when (response) {
                is Response.Error -> response
                is Response.Success -> {
                    Napier.e(response.data.get(0).toString())
                    newsDao.setNewsWithAttachments(response.data.map { it.toNewsWithAttachments() })
                    response
                }
            }
        } catch (e: IOException) {
            Response.Error(ResponseError(message = e.message))
        } catch (e: CustomResponseException){
            Response.Error(ResponseError(message = e.message, code = e.response.status.value))
        } catch (e: ClientRequestException){
            Response.Error(ResponseError(message = e.message))
        } catch (e: HttpRequestTimeoutException){
            Response.Error(ResponseError(message = e.message))
        }

    }


    override suspend fun clearCache() {
        newsDao.clearAll()
    }
}