package ru.esstu.features.account.domain.repo

import ru.esstu.data.web.api.model.Response
import ru.esstu.student.messaging.entities.Sender

interface IAccountInfoApiRepository {
    suspend fun clearUser()

    suspend fun getUser(): Response<Sender>
}