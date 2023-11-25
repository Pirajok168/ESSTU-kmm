package ru.esstu.android.authorized.messaging.new_message.new_appeal.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.esstu.ESSTUSdk
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.repo.INewAppealRepository
import ru.esstu.student.messaging.messenger.new_message.new_appeal.di.newAppealModule
import ru.esstu.student.messaging.messenger.new_message.new_appeal.entities.AppealTheme

data class NewAppealState(
    val departments: List<AppealTheme> = emptyList(),
    val isDepartmentsLoading: Boolean = false,
    val depLoadingError: ResponseError? = null,

    val selectedDepartment: AppealTheme? = null,

    val themes: List<AppealTheme> = emptyList(),
    val isThemesLoading: Boolean = false,
    val themeLoadingError: ResponseError? = null,
    val selectedTheme: AppealTheme? = null,

    val message: String = "",
    val attachments: List<CachedFile> = emptyList(),

    val newAppeal: ConversationPreview? = null,
    val isNewAppealCreating: Boolean = false,
    val appealCreatingError: ResponseError? = null
)

sealed class NewAppealEvents {
    object LoadDepartments : NewAppealEvents()
    object LoadThemes : NewAppealEvents()
    data class PassDepartment(val department: AppealTheme) : NewAppealEvents()
    data class PassTheme(val theme: AppealTheme) : NewAppealEvents()
    data class PassMessage(val message: String) : NewAppealEvents()
    data class PassAttachments(val attachments: List<CachedFile>) : NewAppealEvents()
    data class RemoveAttachment(val attachment: CachedFile) : NewAppealEvents()
    object CreateNewAppeal : NewAppealEvents()
}


class NewAppealViewModel  constructor(
    private val repo: INewAppealRepository = ESSTUSdk.newAppealModule.repo
) : ViewModel() {

    var state by mutableStateOf(NewAppealState())
        private set

    fun onEvent(event: NewAppealEvents) {
        when (event) {
            NewAppealEvents.LoadDepartments -> viewModelScope.launch { onLoadDepartments() }
            NewAppealEvents.LoadThemes -> viewModelScope.launch { onLoadThemes() }
            is NewAppealEvents.PassDepartment -> viewModelScope.launch { onPassDepartment(event.department) }
            is NewAppealEvents.PassTheme -> state = state.copy(selectedTheme = event.theme)
            is NewAppealEvents.PassMessage -> state = state.copy(message = event.message)
            is NewAppealEvents.PassAttachments -> state = state.copy(attachments = (state.attachments + event.attachments).distinct())
            is NewAppealEvents.RemoveAttachment -> state = state.copy(attachments = state.attachments - event.attachment)
            NewAppealEvents.CreateNewAppeal -> viewModelScope.launch { onCreateNewAppeal() }
        }
    }


    private suspend fun onLoadDepartments() {
        repo.loadDepartments().collectLatest { response ->
            state = when (response) {
                is FlowResponse.Error -> state.copy(depLoadingError = response.error)
                is FlowResponse.Loading -> state.copy(isDepartmentsLoading = response.isLoading)
                is FlowResponse.Success -> state.copy(departments = response.data, depLoadingError = null)
            }
        }
    }

    private suspend fun onLoadThemes() {
        val departmentId = state.selectedDepartment?.id ?: return

        repo.loadThemes(departmentId = departmentId).collectLatest { response ->
            state = when (response) {
                is FlowResponse.Error -> state.copy(themeLoadingError = response.error)
                is FlowResponse.Loading -> state.copy(isThemesLoading = response.isLoading)
                is FlowResponse.Success -> {
                    val themes = response.data

                    if (themes.size > 1)
                        state.copy(themes = response.data, themeLoadingError = null)
                    else
                        state.copy(themes = response.data, themeLoadingError = null, selectedTheme = themes.firstOrNull())

                }
            }
        }
    }

    private suspend fun onPassDepartment(department: AppealTheme) {
        state = state.copy(selectedDepartment = department, themes = emptyList(), selectedTheme = null, isThemesLoading = true)
        onLoadThemes()
    }

    private suspend fun onCreateNewAppeal() {

        state = state.copy(isNewAppealCreating = true)

        val theme = state.selectedTheme ?: return
        val appeal = repo.createNewAppeal(
            themeId = theme.id,
            message = state.message,
            attachments = state.attachments
        )

        state = when (appeal) {
            is Response.Error -> state.copy(isNewAppealCreating = false, appealCreatingError = appeal.error)
            is Response.Success -> {
                val response = repo.updateAppealOnPreview(appeal.data)
                when (response) {
                    is Response.Error -> state.copy(isNewAppealCreating = false, appealCreatingError = response.error)
                    is Response.Success -> state.copy(isNewAppealCreating = false, newAppeal = appeal.data, appealCreatingError = null)
                }
            }
        }
    }

}