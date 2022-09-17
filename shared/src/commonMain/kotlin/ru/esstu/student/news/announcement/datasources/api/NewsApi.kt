package ru.esstu.student.news.announcement.datasources.api

import ru.esstu.domain.response.entities.DataResponse


interface NewsApi {

    suspend fun getAnnouncements(
         authToken: String,
         offset: Int,
         limit: Int
    ): DataResponse
}