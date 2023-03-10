package ru.esstu.student.messaging.messenger.dialogs.datasources.repo

import io.github.aakira.napier.Napier
import io.ktor.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.dialogs.entities.PreviewDialog
import kotlin.coroutines.CoroutineContext






class DialogsRepositoryImpl(
    private val dialogApi: IDialogsApiRepository,
    private val dialogDB: IDialogsDbRepository
): IDialogsRepository {
    private val _dialogs = MutableSharedFlow<List<PreviewDialog>>()

    override val dialogs: KotlinNativeFlowWrapper<List<PreviewDialog>>
        get() = KotlinNativeFlowWrapper(_dialogs.asSharedFlow())

    override suspend fun refresh() {

        val recentDialogs = dialogDB.getDialogs(10, 0)
        Napier.e("Данные бд - ${recentDialogs.size}")
        _dialogs.emit(recentDialogs)

        val loadedDialogs = dialogApi.getDialogs(10, 0)

        if (loadedDialogs is Response.Success){
            dialogDB.clear()
            dialogDB.setDialogs(loadedDialogs.data)
            _dialogs.emit(loadedDialogs.data)
        }

    }

    override val iosScope: CoroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = SupervisorJob() + Dispatchers.Main
    }

}