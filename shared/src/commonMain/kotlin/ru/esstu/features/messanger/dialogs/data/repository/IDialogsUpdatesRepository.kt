package ru.esstu.features.messanger.dialogs.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import ru.esstu.data.web.api.model.Response
import ru.esstu.features.messanger.dialogs.domain.model.PreviewDialog

interface IDialogsUpdatesRepository {
    fun installObserving(): Flow<Response<List<PreviewDialog>>>

    fun iosObserving(): KotlinNativeFlowWrapper<Response<List<PreviewDialog>>>

    val iosScope: CoroutineScope
}