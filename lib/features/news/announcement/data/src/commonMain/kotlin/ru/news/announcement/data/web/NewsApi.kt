package ru.news.announcement.data.web


import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.domain.utill.wrappers.Response


interface NewsApi {

    suspend fun getAnnouncements(
         authToken: String,
         offset: Int,
         limit: Int
    ): Response<DataResponse>
}