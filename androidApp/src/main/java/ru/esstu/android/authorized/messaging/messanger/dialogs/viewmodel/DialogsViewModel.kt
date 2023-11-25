package ru.esstu.android.authorized.messaging.messanger.dialogs.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update

import kotlinx.coroutines.launch
import ru.esstu.ESSTUSdk
import ru.esstu.domain.utill.paginator.Paginator
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.student.messaging.messenger.di.messengerModule

import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.IDialogsApiRepository
import ru.esstu.student.messaging.messenger.dialogs.entities.PreviewDialog
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.IDialogsDbRepository
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.IDialogsUpdatesRepository
import ru.esstu.student.messaging.messenger.dialogs.di.dialogsModuleNew


data class DialogState(
    val items: List<PreviewDialog> = emptyList(),
    val pageSize: Int = 10,
    val isLoading: Boolean = false,
    val isEndReached: Boolean = false,
    val error: ResponseError? = null,
    val title: String = "Мессенджер",

    val cleanCacheOnRefresh: Boolean = true
)

sealed class DialogEvents {
    object GetNext : DialogEvents()
    object Reload : DialogEvents()

    object CancelObserving : DialogEvents()
}


class DialogsViewModel constructor(
    private val dialogUpdate: IDialogsUpdatesRepository = ESSTUSdk.dialogsModuleNew.update,

    dialogApi: IDialogsApiRepository = ESSTUSdk.dialogsModuleNew.repo,
    dialogDB: IDialogsDbRepository = ESSTUSdk.dialogsModuleNew.repoDialogs,

    ) : ViewModel() {

    var dialogState by mutableStateOf(DialogState())
        private set


    private val paginator = Paginator(
        initialKey = 0,
        onReset = {
            if (dialogState.cleanCacheOnRefresh)
                dialogDB.clear()
        },
        onLoad = {
            dialogState =
                dialogState.copy(isLoading = it, title = if (it) "Обновление" else "Мессенджер")
        },
        onRequest = { key ->
            val cachedDialogs = dialogDB.getDialogs(dialogState.pageSize, key)

            if (cachedDialogs.isEmpty()) {
                val loadedDialogs = dialogApi.getDialogs(dialogState.pageSize, key)

                if (loadedDialogs is Response.Success)
                    dialogDB.setDialogs(loadedDialogs.data)

                loadedDialogs
            } else
                Response.Success(cachedDialogs)
        },
        getNextKey = { _, _ -> dialogState.items.size },
        onError = { dialogState = dialogState.copy(error = it) },

        onRefresh = { items ->
            dialogState =
                dialogState.copy(items = items, error = null, isEndReached = items.isEmpty())
        },
        onNext = { _, items ->
            dialogState = dialogState.copy(
                items = dialogState.items + items,
                error = null,
                isEndReached = items.isEmpty()
            )
        }
    )


    private var job: Job? = null
    private fun installObserving() {

        if (job?.isActive != true) {
            job = viewModelScope.launch {
                dialogUpdate
                    .installObserving()
                    .collectLatest {
                        if (it is Response.Success && it.data.isNotEmpty()) {
                            dialogState = dialogState.copy(cleanCacheOnRefresh = true)
                            paginator.refresh()
                        }
                    }


            }
        }
    }

    private fun cancelObserving() {
        if (job?.isActive == true)
            job?.cancel()
    }

    fun onEvent(event: DialogEvents) {
        when (event) {
            is DialogEvents.GetNext -> viewModelScope.launch { paginator.loadNext() }
            is DialogEvents.Reload -> viewModelScope.launch {
                installObserving()
                dialogState = dialogState.copy(cleanCacheOnRefresh = false)
                paginator.refresh()
            }
            DialogEvents.CancelObserving -> cancelObserving()
        }
    }
}