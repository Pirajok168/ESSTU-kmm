package ru.esstu.features.firebase.data.api

import ru.esstu.data.web.api.AuthorizedApi
import ru.esstu.data.web.api.model.Response
import ru.esstu.features.firebase.data.model.SendFirebaseTokenRequest

class FirebaseApiImpl(
    val api: AuthorizedApi
) : IFirebaseApi {
    override suspend fun registerFirebaseToken(body: SendFirebaseTokenRequest): Response<Unit> =
        api.post(path = "/lk/api/v1/auth/tokenUpdate", body = body)

}