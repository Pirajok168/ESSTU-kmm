package ru.esstu.student.messaging.messenger.conversations.datasources.repo

import kotlinx.coroutines.CoroutineScope
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.KotlinNativeFlowWrapper

interface IConversationsRepository {
    val conversations: KotlinNativeFlowWrapper<List<ConversationPreview>>

    val iosScope: CoroutineScope
    suspend fun refresh()

    suspend fun getNextPage(offset: Int)
}