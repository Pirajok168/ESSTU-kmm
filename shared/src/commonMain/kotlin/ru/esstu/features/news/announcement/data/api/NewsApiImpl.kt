package ru.esstu.features.news.announcement.data.api

import ru.esstu.data.web.api.AuthorizedApi
import ru.esstu.data.web.api.model.Response
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse

class NewsApiImpl(
    private val authorizedApi: AuthorizedApi
) : NewsApi {
    override suspend fun getAnnouncements(offset: Int, limit: Int): Response<DataResponse> =
        authorizedApi.get(path = "lk/api/v2/messenger/getDialogs?type=ANNOUNCEMENT&offset=${offset}&limit=${limit}")

}