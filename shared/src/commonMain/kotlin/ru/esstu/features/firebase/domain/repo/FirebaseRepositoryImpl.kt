package ru.esstu.features.firebase.domain.repo

import ru.esstu.data.web.api.model.Response
import ru.esstu.features.firebase.data.api.IFirebaseApi
import ru.esstu.features.firebase.data.model.SendFirebaseTokenRequest

class FirebaseRepositoryImpl(
    val api: IFirebaseApi
) : IFirebaseRepository {

    override suspend fun registerFirebaseToken(firebaseToken: String): Response<Unit> =
        api.registerFirebaseToken(SendFirebaseTokenRequest(firebaseToken))
}