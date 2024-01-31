package ru.esstu.features.messanger.appeal.data.db

import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview

interface AppealsCacheDao {


    suspend fun setAppeal(previewDialog: ConversationPreview)

    suspend fun getAppeals(pageSize: Int, pageOffset: Int): List<ConversationPreview>

    suspend fun clearAppeals()

}