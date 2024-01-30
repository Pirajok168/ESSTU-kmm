package ru.esstu.domain.modules.firebase.data.api

import ru.esstu.domain.ktor.AuthorizedApi
import ru.esstu.domain.modules.firebase.data.model.SendFirebaseTokenRequest
import ru.esstu.domain.utill.wrappers.Response

class FirebaseApiImpl(
    val api: AuthorizedApi
): IFirebaseApi {
    override suspend fun registerFirebaseToken(body: SendFirebaseTokenRequest): Response<Unit> =
        api.post(path = "/lk/api/v1/auth/tokenUpdate", body = body)

}