package ru.esstu.student.messaging.messenger.supports.datasource.repo

import com.soywiz.klock.DateTime
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.auth.entities.TokenOwners
import ru.esstu.domain.api.UpdatesApi
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.conversations.datasources.toConversations
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.supports.datasource.db.SupportsTimestampDao

class SupportsUpdatesRepositoryImpl(
    private val auth: IAuthRepository,
    private val api: UpdatesApi,
    private val timestampDao: SupportsTimestampDao
): ISupportsUpdatesRepository {
    override suspend fun installObserving(): Flow<Response<List<ConversationPreview>>> = flow {
        while (true){
            val callTimestamp = DateTime.now().unixMillisLong
            Napier.e("Идёт отслеживание", tag = "Support")
            auth.provideToken { token ->
                val appUserId = (token.owner as? TokenOwners.Student)?.id ?: throw Exception("unsupported User Type")

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
                        Napier.e(result.data.toConversations().toString(), tag = "Support")
                        emit(Response.Success(result.data.toConversations()))
                    }
                }
            }
        }
    }

}