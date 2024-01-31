package ru.esstu.android.authorized.messaging.new_message.selector.viewmoled

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance
import ru.esstu.features.messanger.conversations.domain.interactor.ConversationsInteractor
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview
import ru.esstu.features.messanger.dialogs.domain.interactor.DialogsInteractor
import ru.esstu.student.messaging.entities.Sender
import ru.esstu.student.messaging.group_chat.di.messangerDi


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

class NewMessageViewModel : ViewModel() {

    private val di: DI by lazy { messangerDi() }

    private val dialogsInteractor: DialogsInteractor by di.instance<DialogsInteractor>()
    private val conversationsInteractor: ConversationsInteractor by di.instance<ConversationsInteractor>()

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
        val result = conversationsInteractor.getLocalConversation(newMessageState.recentCount, 0)
        newMessageState = newMessageState.copy(isLoading = false, recentConversations = result)
    }

    private suspend fun onLoadRecentDialogs() {
        newMessageState = newMessageState.copy(isLoading = true)
        val result = dialogsInteractor.getLocalDialogs(newMessageState.recentCount, 0)
        newMessageState = newMessageState.copy(isLoading = false, recentDialogUsers = result.map { it.opponent })
    }
}