package ru.esstu.student.messaging.messenger.dialogs.datasources.repo


import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.student.messaging.messenger.dialogs.datasources.api.DialogsApi
import ru.esstu.student.messaging.messenger.dialogs.datasources.toDialogs
import ru.esstu.student.messaging.messenger.dialogs.entities.Dialog


class DialogsApiRepositoryImpl constructor(
    private val authRepository: IAuthRepository,
    private val dialogsApi: DialogsApi,
) : IDialogsApiRepository {
    override suspend fun getDialogs(limit: Int, offset: Int): Response<List<Dialog>> {

        val response = authRepository.provideToken { type, token ->
            dialogsApi.getDialogs("$token", offset, limit)
        }

        return when(response){
            is Response.Error -> Response.Error(ResponseError(message = response.error.message))
            is Response.Success -> {
                Response.Success(response.data.toDialogs())
            }
        }
    }
}