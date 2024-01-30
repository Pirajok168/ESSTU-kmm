package ru.esstu.domain.modules.firebase.domain.repo

import ru.esstu.domain.modules.firebase.data.api.IFirebaseApi
import ru.esstu.domain.modules.firebase.data.model.SendFirebaseTokenRequest
import ru.esstu.domain.utill.wrappers.Response

class FirebaseRepositoryImpl(
    val api: IFirebaseApi
): IFirebaseRepository {

    override suspend fun registerFirebaseToken(firebaseToken: String): Response<Unit> =
        api.registerFirebaseToken(SendFirebaseTokenRequest(firebaseToken))
}