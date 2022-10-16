package ru.esstu.student.messaging.messenger.dialogs.datasources.repo


import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.student.messaging.messenger.dialogs.datasources.api.DialogsApi
import ru.esstu.student.messaging.messenger.dialogs.datasources.toDialogs



class DialogsApiRepositoryImpl constructor(
    private val authRepository: IAuthRepository,
    private val dialogsApi: DialogsApi,
) : IDialogsApiRepository {
    override suspend fun getDialogs(limit: Int, offset: Int) =
        authRepository.provideToken { type, token ->
            dialogsApi.getDialogs("$token", offset, limit).toDialogs()
        }
}