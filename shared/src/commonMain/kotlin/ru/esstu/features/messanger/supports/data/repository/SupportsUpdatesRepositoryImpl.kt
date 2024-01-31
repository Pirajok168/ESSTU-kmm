package ru.esstu.features.messanger.supports.data.repository

import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import ru.esstu.data.web.api.model.Response
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview
import ru.esstu.features.messanger.dialogs.data.repository.KotlinNativeFlowWrapper
import ru.esstu.features.messanger.supports.domain.toSupports
import ru.esstu.features.update.data.api.UpdatesApi
import kotlin.coroutines.CoroutineContext

class SupportsUpdatesRepositoryImpl(
    private val api: UpdatesApi,
) : ISupportsUpdatesRepository {
    override fun installObserving(): Flow<Response<List<ConversationPreview>>> = flow {
        while (true) {
            val callTimestamp = Clock.System.now().toEpochMilliseconds()
            Napier.e("Идёт отслеживание", tag = "Support")
            val result = api.getUpdates(callTimestamp)

            when (result) {
                is Response.Error -> {
                    emit(Response.Error(result.error))
                    if (result.error.message != "timeout") delay(1000L)
                }

                is Response.Success -> {
                    Napier.e(result.data.toSupports().toString(), tag = "Support")
                    emit(Response.Success(result.data.toSupports()))
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