package ru.esstu.student.messaging.messenger.dialogs.datasources.repo

import kotlinx.coroutines.flow.Flow
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.dialogs.entities.Dialog

interface IDialogsUpdatesRepository {
    val updatesFlow: Flow<Response<List<Dialog>>>
}