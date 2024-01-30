package ru.esstu.domain.modules.firebase.domain.repo

import ru.esstu.domain.utill.wrappers.Response

interface IFirebaseRepository {
    suspend fun registerFirebaseToken(firebaseToken: String): Response<Unit>
    
}