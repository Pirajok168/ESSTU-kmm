package ru.esstu.features.messanger.conversations.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import ru.esstu.data.web.api.model.Response
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview
import ru.esstu.features.messanger.conversations.domain.toConversations
import ru.esstu.features.messanger.dialogs.data.repository.KotlinNativeFlowWrapper
import ru.esstu.features.update.data.api.UpdatesApi
import kotlin.coroutines.CoroutineContext


class ConversationUpdatesRepositoryImpl(
    private val api: UpdatesApi
) : IConversationUpdatesRepository {


    override fun installObserving(): Flow<Response<List<ConversationPreview>>> = flow {
        while (true) {
            val callTimestamp = Clock.System.now().toEpochMilliseconds()
            val result = api.getUpdates(callTimestamp)

            when (result) {
                is Response.Error -> {
                    emit(Response.Error(result.error))
                    if (result.error.message != "timeout") delay(1000L)
                }

                is Response.Success -> {

                    emit(Response.Success(result.data.toConversations()))
                }
            }
        }
    }

    override fun iosObserving(): KotlinNativeFlowWrapper<Response<List<ConversationPreview>>> =
        KotlinNativeFlowWrapper(installObserving())

    override val iosScope: CoroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = SupervisorJob() + Dispatchers.Main
    }


}