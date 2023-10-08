package ru.esstu.student.news.announcement.datasources.repo

import com.soywiz.klock.DateTime
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import ru.esstu.auth.entities.TokenOwners
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.domain.api.UpdatesApi
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.student.news.announcement.datasources.db.timestamp.TimestampDao
import ru.esstu.student.news.announcement.datasources.toAnnouncements
import ru.esstu.student.news.announcement.db.announcement.toTimeStamp
import ru.esstu.student.news.announcement.db.announcement.toTimeStampEntity
import ru.esstu.student.news.entities.NewsNode

class AnnouncementsUpdateRepositoryImpl(
    private val auth: IAuthRepository,
    private val api: UpdatesApi,
    private val timestampDao: TimestampDao,
) : IAnnouncementsUpdateRepository{
    override fun getUpdates() = flow<Response<List<NewsNode>>> {
        while (true) {
            val callTimestamp = DateTime.now().unixMillisLong
            auth.provideToken { token ->

                val appUserId = token.owner.id ?: throw Exception("unsupported User Type")

                val latestTimestamp = timestampDao.getTimestamp(appUserId)?.toTimeStamp() ?: 0L

                val result = auth.provideToken { type, tokenVal ->
                    api.getUpdates("$tokenVal", latestTimestamp).toAnnouncements().asReversed()
                }

                when (result) {
                    is Response.Error -> {
                        emit(Response.Error(result.error))
                        if (result.error.message != "timeout") delay(1000L)
                    }
                    is Response.Success -> {
                        timestampDao.setTimestamp(callTimestamp.toTimeStampEntity(appUserId = appUserId))

                        emit(Response.Success(result.data))
                    }
                }
            }
        }
    }
}