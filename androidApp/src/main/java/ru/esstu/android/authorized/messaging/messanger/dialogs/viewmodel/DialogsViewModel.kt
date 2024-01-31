package ru.esstu.android.authorized.messaging.messanger.dialogs.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.DI
import org.kodein.di.instance
import ru.esstu.android.R
import ru.esstu.data.web.api.model.Response
import ru.esstu.data.web.api.model.ResponseError
import ru.esstu.data.web.handleError.ErrorHandler
import ru.esstu.domain.utill.paginator.Paginator
import ru.esstu.features.messanger.dialogs.data.repository.IDialogsUpdatesRepository
import ru.esstu.features.messanger.dialogs.di.dialogsDi
import ru.esstu.features.messanger.dialogs.domain.interactor.DialogsInteractor
import ru.esstu.features.messanger.dialogs.domain.model.PreviewDialog


data class DialogState(
    val items: List<PreviewDialog> = emptyList(),
    val pageSize: Int = 10,
    val isLoading: Boolean = false,
    val isEndReached: Boolean = false,
    val error: ResponseError? = null,
    val title: Int = R.string.messanger,

    val cleanCacheOnRefresh: Boolean = true
)

sealed class DialogEvents {
    object GetNext : DialogEvents()
    object Reload : DialogEvents()

    object CancelObserving : DialogEvents()
}


class DialogsViewModel : ViewModel() {

    private val di: DI by lazy { dialogsDi() }

    private val dialogApi: DialogsInteractor by di.instance<DialogsInteractor>()

    private val dialogUpdate: IDialogsUpdatesRepository by di.instance<IDialogsUpdatesRepository>()
    private val errorHandler: ErrorHandler by di.instance<ErrorHandler>()


    var dialogState by mutableStateOf(DialogState())
        private set


    private val paginator = Paginator(
        initialKey = 0,
        onReset = {
            if (dialogState.cleanCacheOnRefresh)
                dialogApi.clearDialogs()
        },
        onLoad = {
            dialogState =
                dialogState.copy(
                    isLoading = it,
                    title = if (it) R.string.update else R.string.messanger
                )
        },
        onRequest = { key ->
            errorHandler.makeRequest(request = {
                dialogApi.getDialogs(dialogState.pageSize, key)
            })
        },
        onLocalData = { key ->
            val local = dialogApi.getLocalDialogs(dialogState.pageSize, key)
            withContext(Dispatchers.Main) {
                if (local.isNotEmpty()) {
                    dialogState =
                        dialogState.copy(items = local)
                }
            }
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