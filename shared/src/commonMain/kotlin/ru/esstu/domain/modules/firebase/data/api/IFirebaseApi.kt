package ru.esstu.domain.modules.firebase.data.api

import ru.esstu.domain.modules.firebase.data.model.SendFirebaseTokenRequest
import ru.esstu.domain.utill.wrappers.Response

interface IFirebaseApi {
    suspend fun registerFirebaseToken(body: SendFirebaseTokenRequest): Response<Unit>

}