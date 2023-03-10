package ru.esstu.student.messaging.messenger.dialogs.datasources.repo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import ru.esstu.student.messaging.messenger.dialogs.entities.PreviewDialog

interface IDialogsRepository {
    val dialogs: KotlinNativeFlowWrapper<List<PreviewDialog>>

    val iosScope: CoroutineScope
    suspend fun refresh()
}