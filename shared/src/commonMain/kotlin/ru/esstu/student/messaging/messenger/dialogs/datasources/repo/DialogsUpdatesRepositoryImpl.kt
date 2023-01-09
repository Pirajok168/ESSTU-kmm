package ru.esstu.student.messaging.messenger.dialogs.datasources.repo

import com.soywiz.klock.DateTime
import com.soywiz.klock.DateTimeTz
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.auth.entities.TokenOwners
import ru.esstu.domain.api.UpdatesApi
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.EsstuDatabase

import ru.esstu.student.messaging.messenger.datasources.db.timestamp.TimestampDao

import ru.esstu.student.messaging.messenger.datasources.db.timestamp.toTimeStamp
import ru.esstu.student.messaging.messenger.datasources.db.timestamp.toTimeStampEntity
import ru.esstu.student.messaging.messenger.dialogs.datasources.toDialogs
import ru.esstu.student.messaging.messenger.dialogs.entities.PreviewDialog


class DialogsUpdatesRepositoryImpl(
    auth: IAuthRepository,
    api: UpdatesApi,
    timestampDao: TimestampDao
) : IDialogsUpdatesRepository {

    override val updatesFlow: Flow<Response<List<PreviewDialog>>> = flow {
        while (true) {
            val callTimestamp = DateTime.now().unixMillisLong
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

                        emit(Response.Success(result.data.toDialogs()))
                    }
                }
            }
        }
    }
}