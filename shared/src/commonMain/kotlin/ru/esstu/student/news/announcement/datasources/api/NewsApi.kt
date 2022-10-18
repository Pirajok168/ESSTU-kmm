package ru.esstu.student.news.announcement.datasources.api

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse


interface NewsApi {

    suspend fun getAnnouncements(
         authToken: String,
         offset: Int,
         limit: Int
    ): DataResponse
}