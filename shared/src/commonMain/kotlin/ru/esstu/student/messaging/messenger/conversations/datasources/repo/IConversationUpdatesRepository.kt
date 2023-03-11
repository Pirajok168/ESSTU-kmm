package ru.esstu.student.messaging.messenger.conversations.datasources.repo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.KotlinNativeFlowWrapper
import ru.esstu.student.messaging.messenger.dialogs.entities.PreviewDialog

interface IConversationUpdatesRepository {

    fun installObserving(): Flow<Response<List<ConversationPreview>>>

    fun iosObserving(): KotlinNativeFlowWrapper<Response<List<ConversationPreview>>>

    val iosScope: CoroutineScope
}