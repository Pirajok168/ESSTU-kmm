package ru.esstu.android.authorized.messaging.new_message.new_support.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance
import ru.esstu.data.web.api.model.FlowResponse
import ru.esstu.data.web.api.model.Response
import ru.esstu.data.web.api.model.ResponseError
import ru.esstu.data.web.handleError.ErrorHandler
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.messenger.new_message.new_support.di.newSupportChatDi
import ru.esstu.student.messaging.messenger.new_message.new_support.domain.repo.INewSupportRepository
import ru.esstu.student.messaging.messenger.new_message.new_support.entities.SupportTheme

data class NewSupportState(
    val selectedTheme: SupportTheme? = null,
    val supportThemes: List<SupportTheme> = emptyList(),
    val isThemesLoading: Boolean = false,
    val themeLoadingError: ResponseError? = null,

    val message: String = "",
    val attachments: List<CachedFile> = emptyList(),

    val newSupport: ConversationPreview? = null,
    val isNewSupportCreating: Boolean = false
)

sealed class NewSupportEvents {
    object LoadThemes : NewSupportEvents()
    data class PassTheme(val theme: SupportTheme) : NewSupportEvents()
    data class PassMessage(val message: String) : NewSupportEvents()
    object ClearMessage : NewSupportEvents()
    data class PassAttachments(val attachments: List<CachedFile>) : NewSupportEvents()
    data class RemoveAttachment(val attachment: CachedFile) : NewSupportEvents()
    object CreateNewSupport : NewSupportEvents()
}


class NewSupportViewModel : ViewModel() {

    private val di: DI by lazy { newSupportChatDi() }

    private val repo: INewSupportRepository by di.instance<INewSupportRepository>()
    private val errorHandler: ErrorHandler by di.instance<ErrorHandler>()
    var state by mutableStateOf(NewSupportState())
        private set

    fun onEvent(event: NewSupportEvents) {
        when (event) {
            NewSupportEvents.LoadThemes -> viewModelScope.launch { onLoadThemes() }
            is NewSupportEvents.PassTheme -> state = state.copy(selectedTheme = event.theme)
            is NewSupportEvents.PassMessage -> state = state.copy(message = event.message)
            NewSupportEvents.ClearMessage -> state = state.copy(message = "")
            is NewSupportEvents.PassAttachments -> state =
                state.copy(attachments = (state.attachments + event.attachments).distinct())
            is NewSupportEvents.RemoveAttachment -> state = state.copy(attachments = state.attachments - event.attachment)
            NewSupportEvents.CreateNewSupport -> viewModelScope.launch { onCreateSupport() }
        }
    }

    private suspend fun onLoadThemes() {
        repo.getSupportThemes().collectLatest { response ->
            state = when (response) {
                is FlowResponse.Error -> state.copy(themeLoadingError = response.error)
                is FlowResponse.Loading -> state.copy(isThemesLoading = response.isLoading)
                is FlowResponse.Success -> state.copy(supportThemes = response.data, themeLoadingError = null)
            }
        }
    }

    private suspend fun onCreateSupport() {
        val theme = state.selectedTheme ?: return
        state = state.copy(isNewSupportCreating = true)

        state = when (val support = errorHandler.makeRequest(request = {repo.createNewSupport(themeId = theme.id, state.message, state.attachments)})) {
            is Response.Error -> state.copy(isNewSupportCreating = false, themeLoadingError = support.error)
            is Response.Success -> {
                when (val response = repo.updateSupportsOnPreview(support = support.data)) {
                    is Response.Error -> state.copy(isNewSupportCreating = false, themeLoadingError = response.error)
                    is Response.Success -> state.copy(isNewSupportCreating = false, newSupport = support.data)
                }
            }
        }
    }
}