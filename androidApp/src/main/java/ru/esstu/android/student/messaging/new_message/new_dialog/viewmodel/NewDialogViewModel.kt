package ru.esstu.android.student.messaging.new_message.new_dialog.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.esstu.ESSTUSdk
import ru.esstu.domain.utill.paginator.Paginator
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.entities.Sender
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.IDialogsDbRepository
import ru.esstu.student.messaging.messenger.dialogs.di.dialogsModuleNew
import ru.esstu.student.messaging.messenger.new_message.new_dialog.datasources.repo.INewDialogRepository
import ru.esstu.student.messaging.messenger.new_message.new_dialog.di.newDialogModule
import javax.inject.Inject

data class NewDialogState(
    val opponent: Sender? = null,
    val attachments: List<CachedFile> = emptyList(),
    val message: String = "",
    val isDialogCreating: Boolean = false,
    val messageError: ResponseError? = null,
    val isDialogCreated: Boolean = false,

    val query: String = "",
    val queryPageSize: Int = 10,
    val isQueryPageEndReached: Boolean = false,
    val queryPages: List<Sender> = emptyList(),
    val isQueryLoading: Boolean = false,
    val queryError: ResponseError? = null,

    val isRecentOpponentsLoading: Boolean = false,
    val recentOpponents: List<Sender> = emptyList()
)

sealed class NewDialogEvents {
    data class PassMessage(val message: String) : NewDialogEvents()
    data class PassAttachment(val attachments: List<CachedFile>) : NewDialogEvents()
    data class RemoveAttachment(val attachment: CachedFile) : NewDialogEvents()
    object CreateDialog : NewDialogEvents()

    data class PassQuery(val query: String) : NewDialogEvents()
    object LoadNext : NewDialogEvents()
    object DropQuery : NewDialogEvents()
    data class SelectOpponent(val opponent: Sender) : NewDialogEvents()

    object LoadRecentOpponents : NewDialogEvents()
}

class NewDialogViewModel  constructor(
    private val repo: INewDialogRepository = ESSTUSdk.newDialogModule.repo,
    private val dialogDB: IDialogsDbRepository = ESSTUSdk.dialogsModuleNew.repoDialogs,
) : ViewModel() {

    var state by mutableStateOf(NewDialogState())
        private set

    fun onEvent(event: NewDialogEvents) {
        when (event) {
            is NewDialogEvents.PassAttachment -> state = state.copy(attachments = (state.attachments + event.attachments).distinct())
            is NewDialogEvents.RemoveAttachment -> state = state.copy(attachments = state.attachments - event.attachment)
            is NewDialogEvents.PassMessage -> state = state.copy(message = event.message)
            is NewDialogEvents.PassQuery -> onPassQuery(event.query)
            NewDialogEvents.DropQuery -> viewModelScope.launch { onDropQuery() }
            is NewDialogEvents.SelectOpponent -> state = state.copy(opponent = event.opponent)
            is NewDialogEvents.LoadNext -> viewModelScope.launch { paginator.loadNext() }
            NewDialogEvents.CreateDialog -> viewModelScope.launch { onCreateDialog() }
            NewDialogEvents.LoadRecentOpponents -> viewModelScope.launch { onLoadRecentOpponents() }
        }
    }

    private suspend fun onDropQuery() {
        requestJob?.cancel()
        state = state.copy(query = "", queryPages = emptyList(), isQueryLoading = false, queryError = null)
        paginator.reset()
    }

    private val paginator = Paginator(
        initialKey = 0,
        getNextKey = { key, _ -> key + state.queryPageSize },
        onRequest = { key -> repo.findUsers(state.query, state.queryPageSize, key) },
        onError = { state = state.copy(queryError = it) },
        onLoad = { state = state.copy(isQueryLoading = it) },
        onRefresh = { state = state.copy(queryPages = it, queryError = null, isQueryPageEndReached = it.isEmpty()) },
        onNext = { _, page -> state = state.copy(queryPages = state.queryPages + page, queryError = null, isQueryPageEndReached = page.isEmpty()) }
    )


    private var requestJob: Job? = null
    private fun onPassQuery(query: String) {
        state = state.copy(query = query, isQueryLoading = true, queryPages = emptyList())
        requestJob?.cancel()
        requestJob = viewModelScope.launch {
            delay(500)
            paginator.refresh()
        }
    }

    private suspend fun onCreateDialog() {
        val dialog = state.opponent ?: return
        state = state.copy(isDialogCreating = true, isDialogCreated = false)
        val result = repo.sendMessage(dialog.id, message = state.message, attachments = state.attachments)
        state = when (result) {
            is Response.Error -> state.copy(isDialogCreating = false, isDialogCreated = false, messageError = result.error)
            is Response.Success -> {

                when (val response = repo.updateDialogOnPreview(dialog, result.data)) {
                    is Response.Error -> state.copy(isDialogCreating = false, isDialogCreated = false, messageError = response.error)
                    is Response.Success -> state.copy(isDialogCreating = false, isDialogCreated = true, messageError = null)
                }

            }
        }
    }


    private suspend fun onLoadRecentOpponents() {
        state = state.copy(isRecentOpponentsLoading = true)
        val opponents = dialogDB.getDialogs(state.queryPageSize, 0).map { it.opponent }
        state = state.copy(isRecentOpponentsLoading = false, recentOpponents = opponents)
    }
}