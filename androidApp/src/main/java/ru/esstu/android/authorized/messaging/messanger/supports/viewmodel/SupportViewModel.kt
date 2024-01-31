package ru.esstu.android.authorized.messaging.messanger.supports.viewmodel

import androidx.annotation.StringRes
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
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview
import ru.esstu.features.messanger.supports.data.repository.ISupportsUpdatesRepository
import ru.esstu.features.messanger.supports.di.supportsChatDi
import ru.esstu.features.messanger.supports.domain.interactor.SupportInteractor

data class SupportState(
    val items: List<ConversationPreview> = emptyList(),
    val pageSize: Int = 10,
    val isLoading: Boolean = false,
    val isEndReached: Boolean = false,
    val error: ResponseError? = null,
    val cleanCacheOnRefresh: Boolean = true,
    @StringRes val title: Int = R.string.messanger,
)

sealed class SupportEvents {
    object GetNext : SupportEvents()
    object Reload : SupportEvents()
    object CancelObserving: SupportEvents()
}


class SupportViewModel : ViewModel() {

    private val di: DI by lazy { supportsChatDi() }

    private val supportApi: SupportInteractor by di.instance<SupportInteractor>()

    private val updates: ISupportsUpdatesRepository by di.instance<ISupportsUpdatesRepository>()
    private val errorHandler: ErrorHandler by di.instance<ErrorHandler>()
    var supportState by mutableStateOf(SupportState())
        private set

    private val paginator = Paginator(
        initialKey = 0,
        onReset = { if (supportState.cleanCacheOnRefresh) supportApi.clearSupports() },
        onLoad = {
            supportState = supportState.copy(
                isLoading = it,
                title = if (it) R.string.update else R.string.messanger
            )
        },
        onRequest = { key ->
            errorHandler.makeRequest(request = {
                supportApi.getSupports(supportState.pageSize, key)
            })

        },
        getNextKey = { key, _ -> key + supportState.pageSize },
        onError = { supportState = supportState.copy(error = it) },
        onLocalData = {
            withContext(Dispatchers.Main) {
                supportState = supportState.copy(
                    items = supportApi.getLocalSupports(
                        supportState.pageSize,
                        it
                    )
                )
            }
        },
        onRefresh = { items ->
            supportState =
                supportState.copy(items = items, error = null, isEndReached = items.isEmpty())
        },
        onNext = { _, items ->
            supportState = supportState.copy(
                items = supportState.items + items,
                error = null,
                isEndReached = items.isEmpty()
            )
        }
    )


    private var job: Job? = null

    private fun installObserving(){

        if (job?.isActive != true) {
            job = viewModelScope.launch(Dispatchers.IO) {
                updates
                    .installObserving()
                    .collectLatest {
                        if (it is Response.Success) {
                            withContext(Dispatchers.Main){
                                supportState = supportState.copy(cleanCacheOnRefresh = true)
                                paginator.refresh()
                            }

                        }
                    }


            }
        }
    }
    private fun cancelObserving(){
        if (job?.isActive == true)
            job?.cancel()
    }
    fun onEvent(event: SupportEvents) {
        when (event) {
            is SupportEvents.GetNext -> viewModelScope.launch { paginator.loadNext() }
            SupportEvents.Reload -> viewModelScope.launch {
                installObserving()
                supportState = supportState.copy(cleanCacheOnRefresh = false)
                paginator.refresh()
            }
            SupportEvents.CancelObserving -> cancelObserving()
        }
    }
}


