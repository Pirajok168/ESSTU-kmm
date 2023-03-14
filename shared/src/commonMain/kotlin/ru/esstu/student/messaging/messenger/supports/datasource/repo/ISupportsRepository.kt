package ru.esstu.student.messaging.messenger.supports.datasource.repo

import kotlinx.coroutines.CoroutineScope
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.KotlinNativeFlowWrapper

interface ISupportsRepository {
    val supports: KotlinNativeFlowWrapper<List<ConversationPreview>>
    val iosScope: CoroutineScope

    suspend fun refresh()
    suspend fun getNextPage(offset: Int)
}