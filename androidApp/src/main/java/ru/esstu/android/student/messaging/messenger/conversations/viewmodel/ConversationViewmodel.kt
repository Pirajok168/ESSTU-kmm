package ru.esstu.android.student.messaging.messenger.conversations.viewmodel

import android.util.Log
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
import ru.esstu.student.messaging.messenger.conversations.datasources.repo.IConversationUpdatesRepository
import ru.esstu.student.messaging.messenger.conversations.datasources.repo.IConversationsApiRepository
import ru.esstu.student.messaging.messenger.conversations.datasources.repo.IConversationsDbRepository
import ru.esstu.student.messaging.messenger.conversations.di.conversationModule
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview

data class ConversationState(
    val items: List<ConversationPreview> = emptyList(),
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
    conversationApi: IConversationsApiRepository = ESSTUSdk.conversationModule.repo,
    conversationDb: IConversationsDbRepository = ESSTUSdk.conversationModule.db,
    conversationUpdates: IConversationUpdatesRepository =  ESSTUSdk.conversationModule.update,
) : ViewModel() {
    var conversationState by mutableStateOf(ConversationState())
        private set

    private val paginator = Paginator(
        initialKey = 0,
        onReset = { if (conversationState.cleanCacheOnRefresh) conversationDb.clear() },
        onLoad = { conversationState = conversationState.copy(isLoading = it) },
        onRequest = { key ->

            val cachedConversations = conversationDb.getConversations(conversationState.pageSize, key)
            Log.e("cachedConversations", cachedConversations.toString())
            if (cachedConversations.isEmpty()) {
                val loadedConversations = conversationApi.getConversations(conversationState.pageSize, key)

                if (loadedConversations is Response.Success)
                    conversationDb.setConversations(loadedConversations.data)

                loadedConversations
            } else
                Response.Success(cachedConversations)
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
        viewModelScope.launch {
            conversationUpdates.updatesFlow.collectLatest {
                if (it is Response.Success && it.data.isNotEmpty()) {
                    conversationState = conversationState.copy(cleanCacheOnRefresh = true)
                    paginator.refresh()
                }
            }
        }
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