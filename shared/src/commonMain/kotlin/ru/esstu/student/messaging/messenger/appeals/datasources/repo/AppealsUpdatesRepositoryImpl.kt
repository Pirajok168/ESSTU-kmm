package ru.esstu.student.messaging.messenger.appeals.datasources.repo

import com.soywiz.klock.DateTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.auth.entities.TokenOwners
import ru.esstu.domain.api.UpdatesApi
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.appeals.datasources.db.AppealsTimestampDao
import ru.esstu.student.messaging.messenger.appeals.toAppeals
import ru.esstu.student.messaging.messenger.appeals.toTimeStamp
import ru.esstu.student.messaging.messenger.appeals.toTimeStampEntity
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview


class AppealsUpdatesRepositoryImpl(
    private val auth: IAuthRepository,
    private val api: UpdatesApi,
    private val timestampDao: AppealsTimestampDao
): IAppealUpdatesRepository {


    override suspend fun installObserving(): Flow<Response<List<ConversationPreview>>>  = flow {
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

                        emit(Response.Success(result.data.toAppeals()))
                    }
                }
            }
        }
    }


}