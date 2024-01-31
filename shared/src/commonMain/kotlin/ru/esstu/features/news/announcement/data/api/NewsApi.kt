package ru.esstu.features.news.announcement.data.api

import ru.esstu.data.web.api.model.Response
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse


interface NewsApi {

    suspend fun getAnnouncements(
        offset: Int,
        limit: Int
    ): Response<DataResponse>
}