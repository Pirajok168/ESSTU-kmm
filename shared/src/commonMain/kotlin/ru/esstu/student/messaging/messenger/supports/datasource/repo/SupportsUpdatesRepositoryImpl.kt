package ru.esstu.student.messaging.messenger.supports.datasource.repo

import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.domain.api.UpdatesApi
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.KotlinNativeFlowWrapper
import ru.esstu.student.messaging.messenger.supports.datasource.db.SupportsTimestampDao
import ru.esstu.student.messaging.messenger.supports.toSupports
import kotlin.coroutines.CoroutineContext

class SupportsUpdatesRepositoryImpl(
    private val auth: IAuthRepository,
    private val api: UpdatesApi,
    private val timestampDao: SupportsTimestampDao
): ISupportsUpdatesRepository {
    override  fun installObserving(): Flow<Response<List<ConversationPreview>>> = flow {
        while (true){
            val callTimestamp = Clock.System.now().epochSeconds
            Napier.e("Идёт отслеживание", tag = "Support")
            auth.provideToken { token ->
                val appUserId = token.owner.id ?: throw Exception("unsupported User Type")

                val latestTimestamp = timestampDao.getTimestamp(appUserId)?.toTimeStamp() ?: 0L

                val result = auth.provideToken { type, tokenVal ->
                    api.getUpdates("$tokenVal", latestTimestamp)
                }

                when (result) {
                    is Response.Error -> {
                        emit(Response.Error(result.error))
                        if (result.error.message != "timeout") delay(1000L)
                    }
                    is Response.Success -> {
                        timestampDao.setTimestamp(callTimestamp.toTimeStampEntity(appUserId = appUserId))
                        Napier.e(result.data.toSupports().toString(), tag = "Support")
                        emit(Response.Success(result.data.toSupports()))
                    }
                }
            }
        }
    }

    override fun iosObserving(): KotlinNativeFlowWrapper<Response<List<ConversationPreview>>>
    = KotlinNativeFlowWrapper(installObserving())

    override val iosScope: CoroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = SupervisorJob() + Dispatchers.Main
    }

}