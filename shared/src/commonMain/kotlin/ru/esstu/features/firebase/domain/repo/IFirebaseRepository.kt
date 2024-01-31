package ru.esstu.features.firebase.domain.repo

import ru.esstu.data.web.api.model.Response

interface IFirebaseRepository {
    suspend fun registerFirebaseToken(firebaseToken: String): Response<Unit>

}