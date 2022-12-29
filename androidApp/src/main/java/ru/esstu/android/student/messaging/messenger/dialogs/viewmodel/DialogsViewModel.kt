package ru.esstu.android.student.messaging.messenger.dialogs.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest

import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.esstu.ESSTUSdk
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.IDialogsApiRepository
import ru.esstu.student.messaging.messenger.dialogs.entities.Dialog
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.IDialogsDbRepository
import ru.esstu.student.messaging.messenger.dialogs.di.dialogsModuleNew


data class DialogState(
    val items: List<Dialog> = emptyList(),
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
}


class DialogsViewModel constructor(
    /* dialogUpdate: IDialogsUpdatesRepository = ESSTUSdk.dialogsModule.updateDialogs,
     dialogDB: IDialogsDbRepository = ESSTUSdk.dialogsModule.repoDialogs,*/
    dialogApi: IDialogsApiRepository = ESSTUSdk.dialogsModuleNew.repo,
    dialogDB: IDialogsDbRepository = ESSTUSdk.dialogsModuleNew.repoDialogs,

) : ViewModel() {

    var dialogState by mutableStateOf(DialogState())
        private set

    /*private val paginator = Paginator(
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
    )*/
    private val mutex = Mutex()

    init {
        viewModelScope.launch {
            val cachedDialogs = dialogDB.getDialogs(10, 0)
            dialogState = dialogState.copy(items = cachedDialogs)

        }

        viewModelScope.launch {
            dialogDB._cachedDialogs.collectLatest{
                Log.e("TEST", it.toString())
                dialogState = dialogState.copy(items = it)
            }
        }


        viewModelScope.launch {

            dialogApi.getDialogs(10, 0)
                .collect { data ->
                    mutex.withLock(Dispatchers.Main) {
                        dialogState = when (data) {
                            is FlowResponse.Error -> {
                                dialogState.copy(error = data.error, title = "Соединение...")
                            }
                            is FlowResponse.Loading -> {
                                dialogState.copy(
                                    isLoading = data.isLoading,
                                    title = if (data.isLoading) "Обновление..." else dialogState.title
                                )
                            }
                            is FlowResponse.Success -> {

                                dialogDB.setDialogs(data.data)

                                dialogState.copy(items = data.data, title = "Мессенджер")
                            }
                        }
                    }
                }

        }
        viewModelScope.launch {

            /*dialogUpdate.updatesFlow.collectLatest {
                if (it is Response.Success && it.data.isNotEmpty()) {
                    dialogState = dialogState.copy(cleanCacheOnRefresh = true)
                    paginator.refresh()
                }
            }*/
        }
    }

    fun onEvent(event: DialogEvents) {
        /*when (event) {
            is DialogEvents.GetNext -> viewModelScope.launch { paginator.loadNext() }
            is DialogEvents.Reload -> viewModelScope.launch {
                dialogState = dialogState.copy(cleanCacheOnRefresh = false)
                paginator.refresh()
            }
        }*/
    }
}