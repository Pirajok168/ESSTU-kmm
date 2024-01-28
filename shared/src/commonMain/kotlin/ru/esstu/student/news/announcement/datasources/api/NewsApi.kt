package ru.esstu.student.news.announcement.datasources.api

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.domain.utill.wrappers.Response


interface NewsApi {

    suspend fun getAnnouncements(
         offset: Int,
         limit: Int
    ): Response<DataResponse>
}