package ru.esstu.android.authorized.messaging.messanger.conversations.viewmodel

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
import ru.esstu.features.messanger.conversations.data.repository.IConversationUpdatesRepository
import ru.esstu.features.messanger.conversations.di.conversationsChatDi
import ru.esstu.features.messanger.conversations.domain.interactor.ConversationsInteractor
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview

data class ConversationState(
    val items: List<ConversationPreview> = emptyList(),
    val pageSize: Int = 10,
    val isLoading: Boolean = false,
    val isEndReached: Boolean = false,
    val error: ResponseError? = null,
    val cleanCacheOnRefresh: Boolean = true,
    @StringRes val title: Int = R.string.messanger,
)

sealed class ConversationEvents {
    object GetNext : ConversationEvents()
    object Reload : ConversationEvents()
    object CancelObserving : ConversationEvents()
}


class ConversationViewModel : ViewModel() {

    private val di: DI by lazy { conversationsChatDi() }

    private val conversationsInteractor: ConversationsInteractor by di.instance<ConversationsInteractor>()
    private val conversationUpdates: IConversationUpdatesRepository by di.instance<IConversationUpdatesRepository>()
    private val errorHandler: ErrorHandler by di.instance<ErrorHandler>()
    var conversationState by mutableStateOf(ConversationState())
        private set

    private val paginator = Paginator(
        initialKey = 0,
        onReset = {
            if (conversationState.cleanCacheOnRefresh)
                conversationsInteractor.clearConversation()
        },
        onLoad = {
            conversationState = conversationState.copy(
                isLoading = it,
                title = if (it) R.string.update else R.string.messanger
            )
        },
        onRequest = { key ->
            errorHandler.makeRequest(request = {
                conversationsInteractor.getConversation(
                    limit = conversationState.pageSize,
                    offset = key
                )
            })
        },
        onLocalData = { key ->
            val local =
                conversationsInteractor.getLocalConversation(conversationState.pageSize, key)
            withContext(Dispatchers.Main) {
                conversationState = conversationState.copy(items = local)
            }
        },
        getNextKey = { key, _ -> key + conversationState.pageSize },
        onError = { conversationState = conversationState.copy(error = it) },

        onRefresh = { items ->
            conversationState =
                conversationState.copy(items = items, error = null, isEndReached = items.isEmpty())
        },
        onNext = { _, items ->
            conversationState = conversationState.copy(
                items = conversationState.items + items,
                error = null,
                isEndReached = items.isEmpty()
            )
        }
    )


    private var job: Job? = null
    private fun installObserving() {

        if (job?.isActive != true) {
            job = viewModelScope.launch {
                conversationUpdates
                    .installObserving()
                    .collectLatest {
                        if (it is Response.Success && it.data.isNotEmpty()) {
                            conversationState = conversationState.copy(cleanCacheOnRefresh = true)
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

    fun onEvent(event: ConversationEvents) {
        when (event) {
            is ConversationEvents.GetNext -> viewModelScope.launch { paginator.loadNext() }
            ConversationEvents.Reload -> {
                installObserving()
                viewModelScope.launch {
                    conversationState = conversationState.copy(cleanCacheOnRefresh = false)
                    paginator.refresh()
                }
            }

            ConversationEvents.CancelObserving -> {
                cancelObserving()
            }
        }
    }
}