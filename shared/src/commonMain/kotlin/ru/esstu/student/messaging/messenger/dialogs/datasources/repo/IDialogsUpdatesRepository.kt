package ru.esstu.student.messaging.messenger.dialogs.datasources.repo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.dialogs.entities.PreviewDialog

interface IDialogsUpdatesRepository {
     fun installObserving(): Flow<Response<List<PreviewDialog>>>

     fun iosObserving(): KotlinNativeFlowWrapper<Response<List<PreviewDialog>>>

    val iosScope: CoroutineScope
}