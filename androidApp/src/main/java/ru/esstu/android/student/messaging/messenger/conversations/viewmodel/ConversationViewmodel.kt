package ru.esstu.android.student.messaging.messenger.conversations.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.esstu.ESSTUSdk
import ru.esstu.domain.utill.paginator.Paginator
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.student.messaging.messenger.conversations.datasources.repo.IConversationApiRepository
import ru.esstu.student.messaging.messenger.conversations.di.conversationModule
import ru.esstu.student.messaging.messenger.conversations.entities.Conversation
import javax.inject.Inject

data class ConversationState(
    val items: List<Conversation> = emptyList(),
    val pageSize: Int = 10,
    val isLoading: Boolean = false,
    val isEndReached: Boolean = false,
    val error: ResponseError? = null,
    val cleanCacheOnRefresh: Boolean = true
)

sealed class ConversationEvents {
    object GetNext : ConversationEvents()
    object Reload : ConversationEvents()
}


class ConversationViewModel  constructor(
    conversationApi: IConversationApiRepository = ESSTUSdk.conversationModule.repo,
    //conversationDb: IConversationDbRepository,
    //conversationUpdates: IConversationUpdatesRepository
) : ViewModel() {
    var conversationState by mutableStateOf(ConversationState())
        private set

    private val paginator = Paginator(
        initialKey = 0,
        onReset = { /*if (conversationState.cleanCacheOnRefresh) conversationDb.clear()*/ },
        onLoad = { conversationState = conversationState.copy(isLoading = it) },
        onRequest = { key ->
            val loadedConversations = conversationApi.getConversations(conversationState.pageSize, key)
            loadedConversations
            /*val cachedConversations = conversationDb.getConversations(conversationState.pageSize, key)

            if (cachedConversations.isEmpty()) {
                val loadedConversations = conversationApi.getConversations(conversationState.pageSize, key)

                if (loadedConversations is Response.Success)
                    conversationDb.setConversations(loadedConversations.data.mapIndexed { index, conversation -> index + key to conversation }.toMap())

                loadedConversations
            } else
                Response.Success(cachedConversations)*/
        },
        getNextKey = { key, _ -> key + conversationState.pageSize },
        onError = { conversationState = conversationState.copy(error = it) },

        onRefresh = { items ->
            conversationState = conversationState.copy(items = items, error = null, isEndReached = items.isEmpty())
        },
        onNext = { _, items ->
            conversationState = conversationState.copy(items = conversationState.items + items, error = null, isEndReached = items.isEmpty())
        }
    )

    init {
        /*viewModelScope.launch {
            conversationUpdates.updatesFlow.collectLatest {
                if (it is Response.Success && it.data.isNotEmpty()) {
                    conversationState = conversationState.copy(cleanCacheOnRefresh = true)
                    paginator.refresh()
                }
            }
        }*/
    }

    fun onEvent(event: ConversationEvents) {
        when (event) {
            is ConversationEvents.GetNext -> viewModelScope.launch { paginator.loadNext() }
            ConversationEvents.Reload -> viewModelScope.launch {
                conversationState = conversationState.copy(cleanCacheOnRefresh = false)
                paginator.refresh()
            }
        }
    }
}