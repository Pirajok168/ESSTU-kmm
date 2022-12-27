package ru.esstu.student.messaging.messenger.dialogs.datasources.repo

import ru.esstu.student.messaging.messenger.dialogs.entities.Dialog

interface IDialogsDbRepository {
    suspend fun getDialogs(limit: Int, offset: Int): List<Dialog>

    suspend fun clear()

    suspend fun setDialogs(dialogs: Map<Int, Dialog>)
}