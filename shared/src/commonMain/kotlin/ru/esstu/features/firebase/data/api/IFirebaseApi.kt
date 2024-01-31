package ru.esstu.features.firebase.data.api

import ru.esstu.data.web.api.model.Response
import ru.esstu.features.firebase.data.model.SendFirebaseTokenRequest

interface IFirebaseApi {
    suspend fun registerFirebaseToken(body: SendFirebaseTokenRequest): Response<Unit>

}