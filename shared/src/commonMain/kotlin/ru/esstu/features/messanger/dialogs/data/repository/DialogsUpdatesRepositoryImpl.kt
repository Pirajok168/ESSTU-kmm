package ru.esstu.features.messanger.dialogs.data.repository

import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Clock
import ru.esstu.data.web.api.model.Response
import ru.esstu.data.web.api.model.doOnError
import ru.esstu.data.web.api.model.doOnSuccess
import ru.esstu.features.messanger.dialogs.domain.model.PreviewDialog
import ru.esstu.features.messanger.dialogs.domain.toDialogs
import ru.esstu.features.update.data.api.UpdatesApi
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
    private val api: UpdatesApi
) : IDialogsUpdatesRepository {


    override fun installObserving(): Flow<Response<List<PreviewDialog>>> = flow {
        while (true) {
            Napier.e("DialogsUpdatesRepositoryImpl", tag = "lifecycle")
            val callTimestamp = Clock.System.now().toEpochMilliseconds()
            api.getUpdates(callTimestamp)
                .doOnSuccess {

                    emit(Response.Success(it.toDialogs()))
                }
                .doOnError {
                    emit(Response.Error(it))
                    delay(1000L)
                }
        }
    }

    override fun iosObserving(): KotlinNativeFlowWrapper<Response<List<PreviewDialog>>> =
        KotlinNativeFlowWrapper(installObserving())

    override val iosScope: CoroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = SupervisorJob() + Dispatchers.Main
    }
}