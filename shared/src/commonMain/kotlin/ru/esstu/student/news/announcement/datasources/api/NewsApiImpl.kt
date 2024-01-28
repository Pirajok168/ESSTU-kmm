package ru.esstu.student.news.announcement.datasources.api

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.domain.ktor.AuthorizedApi
import ru.esstu.domain.utill.wrappers.Response

class NewsApiImpl(
    private val authorizedApi: AuthorizedApi
) : NewsApi {
    override suspend fun getAnnouncements(offset: Int, limit: Int): Response<DataResponse> =
        authorizedApi.get(path = "lk/api/v2/messenger/getDialogs?type=ANNOUNCEMENT&offset=${offset}&limit=${limit}")

}