package ru.esstu.domain.modules.account.datasources.repo

import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.User

interface IAccountInfoApiRepository {
    suspend fun clearUser()

    suspend fun getUser(): Response<User>
}