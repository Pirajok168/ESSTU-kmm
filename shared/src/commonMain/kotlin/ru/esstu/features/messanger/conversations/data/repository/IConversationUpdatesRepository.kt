package ru.esstu.features.messanger.conversations.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import ru.esstu.data.web.api.model.Response
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview
import ru.esstu.features.messanger.dialogs.data.repository.KotlinNativeFlowWrapper

interface IConversationUpdatesRepository {

    fun installObserving(): Flow<Response<List<ConversationPreview>>>

    fun iosObserving(): KotlinNativeFlowWrapper<Response<List<ConversationPreview>>>

    val iosScope: CoroutineScope
}