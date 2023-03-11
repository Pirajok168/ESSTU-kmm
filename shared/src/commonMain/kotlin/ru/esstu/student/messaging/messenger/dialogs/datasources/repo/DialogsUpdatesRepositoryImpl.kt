package ru.esstu.student.messaging.messenger.dialogs.datasources.repo

import com.soywiz.klock.DateTime
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.auth.entities.TokenOwners
import ru.esstu.domain.api.UpdatesApi
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.DialogsTimestampDao
import ru.esstu.student.messaging.messenger.dialogs.datasources.toDialogs
import ru.esstu.student.messaging.messenger.dialogs.datasources.toTimeStamp
import ru.esstu.student.messaging.messenger.dialogs.datasources.toTimeStampEntity
import ru.esstu.student.messaging.messenger.dialogs.entities.PreviewDialog
import kotlin.coroutines.CoroutineContext

class KotlinNativeFlowWrapper<T>(private val flow: Flow<T>) {
    fun subscribe(
        scope: CoroutineScope,
        onEach: (item: T) -> Unit,
        onComplete: () -> Unit,
        onThrow: (error: Throwable) -> Unit
    ) = flow
        .conflate()
        .onEach { onEach(it) }
        .catch { onThrow(it) }
        .onCompletion { onComplete() }
        .launchIn(scope)
}



class DialogsUpdatesRepositoryImpl(
    private val auth: IAuthRepository,
    private val api: UpdatesApi,
    private val timestampDao: DialogsTimestampDao
) : IDialogsUpdatesRepository {


    override fun installObserving(): Flow<Response<List<PreviewDialog>>> = flow {
        while (true) {
            Napier.e("DialogsUpdatesRepositoryImpl", tag = "lifecycle")
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

    override fun iosObserving(): KotlinNativeFlowWrapper<Response<List<PreviewDialog>>>
        = KotlinNativeFlowWrapper(installObserving())

    override val iosScope: CoroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = SupervisorJob() + Dispatchers.Main
    }
}