package ru.esstu.android.authorized.messaging.messanger.appeals.viewmodel

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
import ru.esstu.features.messanger.appeal.data.repository.IAppealUpdatesRepository
import ru.esstu.features.messanger.appeal.di.appealDi
import ru.esstu.features.messanger.appeal.domain.interactor.AppealsInteractor
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview

data class AppealState(
    val items: List<ConversationPreview> = emptyList(),
    val pageSize: Int = 10,
    val isLoading: Boolean = false,
    val isEndReached: Boolean = false,
    val error: ResponseError? = null,
    val cleanCacheOnRefresh: Boolean = true,
    @StringRes val title: Int = R.string.messanger,
)

sealed class AppealEvents {
    object GetNext : AppealEvents()
    object Reload : AppealEvents()
    object CancelObserving : AppealEvents()
}


class AppealsViewModel : ViewModel() {
    private val di: DI by lazy { appealDi() }

    private val appealsInteractor: AppealsInteractor by di.instance<AppealsInteractor>()
    private val updates: IAppealUpdatesRepository by di.instance<IAppealUpdatesRepository>()
    private val errorHandler: ErrorHandler by di.instance<ErrorHandler>()


    var appealsState by mutableStateOf(AppealState())
        private set

    private val paginator = Paginator(
        initialKey = 0,
        onReset = { if (appealsState.cleanCacheOnRefresh) appealsInteractor.clearAppeals() },
        onLoad = {
            appealsState = appealsState.copy(
                isLoading = it,
                title = if (it) R.string.update else R.string.messanger
            )
        },
        onRequest = { key ->
            errorHandler.makeRequest(request = {
                appealsInteractor.getAppeals(
                    appealsState.pageSize,
                    key
                )
            })
        },
        getNextKey = { key, _ -> key + appealsState.pageSize },
        onError = { appealsState = appealsState.copy(error = it) },
        onLocalData = {
            withContext(Dispatchers.Main) {
                appealsState =
                    appealsState.copy(
                        items = appealsInteractor.getLocalAppeals(
                            appealsState.pageSize,
                            it
                        )
                    )
            }


        },
        onRefresh = { items ->
            appealsState =
                appealsState.copy(items = items, error = null, isEndReached = items.isEmpty())
        },
        onNext = { _, items ->
            appealsState = appealsState.copy(
                items = appealsState.items + items,
                error = null,
                isEndReached = items.isEmpty()
            )
        }
    )


    private var job: Job? = null

    private fun installObserving() {

        if (job?.isActive != true) {
            job = viewModelScope.launch(Dispatchers.IO) {
                updates
                    .installObserving()
                    .collectLatest {
                        if (it is Response.Success) {
                            withContext(Dispatchers.Main) {
                                appealsState = appealsState.copy(cleanCacheOnRefresh = true)
                                paginator.refresh()
                            }

                        }
                    }


            }
        }
    }

    private fun cancelObserving() {
        if (job?.isActive == true)
            job?.cancel()
    }

    fun onEvent(event: AppealEvents) {
        when (event) {
            is AppealEvents.GetNext -> viewModelScope.launch { paginator.loadNext() }
            AppealEvents.Reload -> viewModelScope.launch {
                installObserving()
                appealsState = appealsState.copy(cleanCacheOnRefresh = false)
                paginator.refresh()
            }

            AppealEvents.CancelObserving -> cancelObserving()
        }
    }
}