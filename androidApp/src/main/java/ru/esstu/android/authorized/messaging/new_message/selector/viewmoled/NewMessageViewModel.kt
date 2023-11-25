package ru.esstu.android.authorized.messaging.new_message.selector.viewmoled

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.esstu.ESSTUSdk
import ru.esstu.student.messaging.entities.Sender
import ru.esstu.student.messaging.messenger.conversations.datasources.repo.IConversationsDbRepository
import ru.esstu.student.messaging.messenger.conversations.di.conversationModule
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.IDialogsDbRepository
import ru.esstu.student.messaging.messenger.dialogs.di.dialogsModuleNew


data class NewMessageState(
    val isLoading: Boolean = false,
    val recentDialogUsers: List<Sender> = emptyList(),
    val recentConversations: List<ConversationPreview> = emptyList(),
    val recentCount: Int = 4,
)

sealed class NewMessageEvents {
    object LoadRecentDialogs : NewMessageEvents()
    object LoadRecentConversations : NewMessageEvents()
}

class NewMessageViewModel  constructor(
    private val dialogDB: IDialogsDbRepository = ESSTUSdk.dialogsModuleNew.repoDialogs,
    private val conversationDB: IConversationsDbRepository = ESSTUSdk.conversationModule.db
) : ViewModel() {

    var newMessageState by mutableStateOf(NewMessageState())
        private set

    fun onEvent(event: NewMessageEvents) {
        when (event) {
            is NewMessageEvents.LoadRecentDialogs -> viewModelScope.launch { onLoadRecentDialogs() }
            is NewMessageEvents.LoadRecentConversations -> viewModelScope.launch { onLoadRecentConversations() }
        }
    }

    private suspend fun onLoadRecentConversations() {
        newMessageState = newMessageState.copy(isLoading = true)
        val result = conversationDB.getConversations(newMessageState.recentCount, 0)
        newMessageState = newMessageState.copy(isLoading = false, recentConversations = result)
    }

    private suspend fun onLoadRecentDialogs() {
        newMessageState = newMessageState.copy(isLoading = true)
        val result = dialogDB.getDialogs(newMessageState.recentCount, 0)
        newMessageState = newMessageState.copy(isLoading = false, recentDialogUsers = result.map { it.opponent })
    }
}