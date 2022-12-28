package ru.esstu.student.messaging.messenger.dialogs.datasources.repo

import kotlinx.coroutines.flow.Flow
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.student.messaging.messenger.dialogs.entities.Dialog

interface IDialogsApiRepository {
    suspend fun getDialogs(limit: Int, offset: Int): Flow<FlowResponse<List<Dialog>>>
}