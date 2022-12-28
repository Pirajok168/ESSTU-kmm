package ru.esstu.student.messaging.messenger.dialogs.datasources.repo


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.student.messaging.messenger.dialogs.datasources.toDialogs
import ru.esstu.student.messaging.messenger.dialogs.entities.Dialog
import ru.esstu.student.messaging.messenger.dialogs.datasources.api.DialogsApi


class DialogsApiRepositoryImpl constructor(
    private val authRepository: IAuthRepository,
    private val dialogsApi: DialogsApi,
) : IDialogsApiRepository {
    override suspend fun getDialogs(limit: Int, offset: Int): Flow<FlowResponse<List<Dialog>>> = flow {
        emit(FlowResponse.Loading())
        val response = authRepository.provideToken { type, token ->
            dialogsApi.getDialogs("$token", offset, limit)
        }

        when(response){
            is Response.Error -> emit(FlowResponse.Error(ResponseError(message = response.error.message)))
            is Response.Success -> {
                emit(FlowResponse.Success(response.data.toDialogs()))
            }
        }

        emit(FlowResponse.Loading(false))
    }
}