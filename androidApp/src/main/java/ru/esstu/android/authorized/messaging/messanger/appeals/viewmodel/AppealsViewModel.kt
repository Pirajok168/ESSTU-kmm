package ru.esstu.android.authorized.messaging.messanger.appeals.viewmodel

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
import ru.esstu.ESSTUSdk
import ru.esstu.domain.handleError.ErrorHandler
import ru.esstu.domain.ktor.domainApi
import ru.esstu.domain.utill.paginator.Paginator
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.student.messaging.messenger.appeals.datasources.repo.IAppealUpdatesRepository
import ru.esstu.student.messaging.messenger.appeals.datasources.repo.IAppealsApiRepository
import ru.esstu.student.messaging.messenger.appeals.datasources.repo.IAppealsDbRepository
import ru.esstu.student.messaging.messenger.appeals.di.appealsModule
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview

data class AppealState(
    val items: List<ConversationPreview> = emptyList(),
    val pageSize: Int = 10,
    val isLoading: Boolean = false,
    val isEndReached: Boolean = false,
    val error: ResponseError? = null,
    val cleanCacheOnRefresh: Boolean = true
)

sealed class AppealEvents {
    object GetNext : AppealEvents()
    object Reload : AppealEvents()
    object CancelObserving: AppealEvents()
}


class AppealsViewModel constructor(
    appealApi: IAppealsApiRepository = ESSTUSdk.appealsModule.repo,
    appealDb: IAppealsDbRepository = ESSTUSdk.appealsModule.db,
    private val updates: IAppealUpdatesRepository = ESSTUSdk.appealsModule.update,
    private val errorHandler: ErrorHandler = ESSTUSdk.domainApi.errorHandler
) : ViewModel() {
    var appealsState by mutableStateOf(AppealState())
        private set

    private val paginator = Paginator(
        initialKey = 0,
        onReset = { if (appealsState.cleanCacheOnRefresh) appealDb.clear() },
        onLoad = { appealsState = appealsState.copy(isLoading = it) },
        onRequest = { key ->
            val cachedConversations = appealDb.getAppeals(appealsState.pageSize, key)

            if (cachedConversations.isEmpty()) {
                val loadedConversations = errorHandler.makeRequest(
                    request = {
                        appealApi.getAppeals(appealsState.pageSize, key)
                    }
                )
                if (loadedConversations is Response.Success)
                    appealDb.setAppeals(loadedConversations.data)

                loadedConversations
            } else
                Response.Success(cachedConversations)
        },
        getNextKey = { key, _ -> key + appealsState.pageSize },
        onError = { appealsState = appealsState.copy(error = it) },

        onRefresh = { items ->
            appealsState = appealsState.copy(items = items, error = null, isEndReached = items.isEmpty())
        },
        onNext = { _, items ->
            appealsState = appealsState.copy(items = appealsState.items + items, error = null, isEndReached = items.isEmpty())
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
                                appealsState = appealsState.copy(cleanCacheOnRefresh = true)
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
    fun onEvent(event: AppealEvents) {
        when (event) {
            is AppealEvents.GetNext -> viewModelScope.launch { paginator.loadNext() }
            AppealEvents.Reload -> viewModelScope.launch {
                installObserving()
                appealsState = appealsState.copy(cleanCacheOnRefresh = true)
                paginator.refresh()
            }
            AppealEvents.CancelObserving -> cancelObserving()
        }
    }
}