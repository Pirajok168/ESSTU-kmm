package ru.esstu.android.student.messaging.messenger.dialogs.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.esstu.ESSTUSdk
import ru.esstu.domain.utill.paginator.Paginator
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.student.messaging.dialog_chat.datasources.di.dialogChatModule
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.IDialogsApiRepository
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.IDialogsDbRepository
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.IDialogsUpdatesRepository
import ru.esstu.student.messaging.messenger.dialogs.di.dialogsModule
import ru.esstu.student.messaging.messenger.dialogs.entities.Dialog


data class DialogState(
    val items: List<Dialog> = emptyList(),
    val pageSize: Int = 10,
    val isLoading: Boolean = false,
    val isEndReached: Boolean = false,
    val error: ResponseError? = null,

    val cleanCacheOnRefresh: Boolean = true
)

sealed class DialogEvents {
    object GetNext : DialogEvents()
    object Reload : DialogEvents()
}


class DialogViewModel  constructor(
    dialogUpdate: IDialogsUpdatesRepository = ESSTUSdk.dialogsModule.updateDialogs,
    dialogDB: IDialogsDbRepository = ESSTUSdk.dialogsModule.repoDialogs,
    dialogApi: IDialogsApiRepository = ESSTUSdk.dialogsModule.repo
) : ViewModel() {

    var dialogState by mutableStateOf(DialogState())
        private set

    private val paginator = Paginator(
        initialKey = 0,
        onReset = { if (dialogState.cleanCacheOnRefresh) dialogDB.clear() },
        onLoad = { dialogState = dialogState.copy(isLoading = it) },
        onRequest = { key ->
            val cachedDialogs = dialogDB.getDialogs(dialogState.pageSize, key)

            if (cachedDialogs.isEmpty()) {
                val loadedDialogs = dialogApi.getDialogs(dialogState.pageSize, key)

                if (loadedDialogs is Response.Success)
                    dialogDB.setDialogs(loadedDialogs.data.mapIndexed { index, dialog -> key + index to dialog }.toMap())

                loadedDialogs
            } else
                Response.Success(cachedDialogs)
        },
        getNextKey = { _, _ -> dialogState.items.size },
        onError = { dialogState = dialogState.copy(error = it) },

        onRefresh = { items ->
            dialogState = dialogState.copy(items = items, error = null, isEndReached = items.isEmpty())
        },
        onNext = { _, items ->
            dialogState = dialogState.copy(items = dialogState.items + items, error = null, isEndReached = items.isEmpty())
        }
    )

    init {
        viewModelScope.launch {
            when(val data = dialogApi.getDialogs(10,0)){
                is Response.Error -> {

                }
                is Response.Success -> {
                    dialogState = dialogState.copy(items = data.data)
                }
            }
        }
        viewModelScope.launch {

            dialogUpdate.updatesFlow.collectLatest {
                if (it is Response.Success && it.data.isNotEmpty()) {
                    dialogState = dialogState.copy(cleanCacheOnRefresh = true)
                    paginator.refresh()
                }
            }
        }
    }

    fun onEvent(event: DialogEvents) {
        when (event) {
            is DialogEvents.GetNext -> viewModelScope.launch { paginator.loadNext() }
            is DialogEvents.Reload -> viewModelScope.launch {
                dialogState = dialogState.copy(cleanCacheOnRefresh = false)
                paginator.refresh()
            }
        }
    }
}