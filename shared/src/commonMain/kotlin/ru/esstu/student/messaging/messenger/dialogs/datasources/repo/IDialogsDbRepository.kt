package ru.esstu.student.messaging.messenger.dialogs.datasources.repo

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import ru.esstu.student.messaging.messenger.dialogs.entities.Dialog

interface IDialogsDbRepository {
    val _cachedDialogs: SharedFlow<List<Dialog>>
    suspend fun getDialogs(limit: Int, offset: Int): List<Dialog>

    suspend fun clear()

    suspend fun setDialogs(dialogs: List<Dialog>)
}