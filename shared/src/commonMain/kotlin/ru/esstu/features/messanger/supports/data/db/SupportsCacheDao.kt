package ru.esstu.features.messanger.supports.data.db

import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview


interface SupportsCacheDao {
    suspend fun setSupport(previewDialog: ConversationPreview)

    suspend fun getSupports(pageSize: Int, pageOffset: Int): List<ConversationPreview>

    suspend fun clearSupports()
}