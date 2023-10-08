package ru.esstu.student.messaging.messenger.conversations.datasources.repo

import com.soywiz.klock.DateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.auth.entities.TokenOwners
import ru.esstu.domain.api.UpdatesApi
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.conversations.datasources.db.ConversationsTimestampDao
import ru.esstu.student.messaging.messenger.conversations.datasources.toConversations
import ru.esstu.student.messaging.messenger.conversations.datasources.toTimeStamp
import ru.esstu.student.messaging.messenger.conversations.datasources.toTimeStampEntity
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.KotlinNativeFlowWrapper
import ru.esstu.student.messaging.messenger.dialogs.entities.PreviewDialog
import kotlin.coroutines.CoroutineContext


class ConversationUpdatesRepositoryImpl(
    private val auth: IAuthRepository,
    private val api: UpdatesApi,
    private val timestampDao: ConversationsTimestampDao
): IConversationUpdatesRepository {


    override  fun installObserving(): Flow<Response<List<ConversationPreview>>>  = flow {
        while (true){
            val callTimestamp = DateTime.now().unixMillisLong
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

                        emit(Response.Success(result.data.toConversations()))
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