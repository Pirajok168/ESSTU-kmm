package ru.esstu.android.authorized.profile.student.portfolio.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.esstu.ESSTUSdk
import ru.esstu.android.authorized.profile.student.portfolio.state.StudentPortfolioState
import ru.esstu.domain.handleError.ErrorHandler
import ru.esstu.domain.ktor.domainApi
import ru.esstu.domain.utill.wrappers.doOnError
import ru.esstu.domain.utill.wrappers.doOnSuccess
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.profile.porfolio.domain.di.portfolioModule
import ru.esstu.student.profile.porfolio.domain.model.StudentPortfolioType
import ru.esstu.student.profile.porfolio.domain.repository.IPortfolioRepository

class StudentPortfolioViewModel : ViewModel() {

    private val portfolioRepository: IPortfolioRepository = ESSTUSdk.portfolioModule.repo
    private val errorHandler: ErrorHandler = ESSTUSdk.domainApi.errorHandler

    var state by mutableStateOf(StudentPortfolioState())
        private set

    fun preDisplayFile(type: StudentPortfolioType) {
        state = state.copy(type = type, listFiles = emptyList(), isLoad = true)
        viewModelScope.launch {
            errorHandler.makeRequest(request = {
                portfolioRepository.getFilesStudentPortfolioByType(type)
            })
                .doOnSuccess {
                    withContext(Dispatchers.Main) {
                        state = state.copy(listFiles = it, isLoad = false)
                    }
                }
        }
    }

    fun onPassAttachments(attachments: List<CachedFile>) {
        state = state.copy(attachments = state.attachments + attachments)
    }

    fun onRemoveAttachment(file: CachedFile) {
        state = state.copy(attachments = state.attachments.filterNot { it == file })
    }

    fun onSetDate(value: Long) {
        state = state.copy(date = value)
    }

    fun onSetTheme(value: String) {
        state = state.copy(theme = value)
    }

    fun onSetStatus(value: String) {
        state = state.copy(status = value)
    }

    fun onInputName(value: String) {
        state = state.copy(name = value)
    }

    fun onInputExhibit(value: String) {
        state = state.copy(exhibit = value)
    }

    fun onInputPlace(value: String) {
        state = state.copy(place = value)
    }

    fun onInputCoathor(value: String) {
        state = state.copy(coathor = value)
    }

    fun onInputStartDate(value: Long) {
        state = state.copy(date = value)
    }

    fun onInputResult(value: String) {
        state = state.copy(result = value)
    }

    fun onInputEndDate(value: Long) {
        state = state.copy(endDate = value)
    }

    fun onInputTypeWork(value: String) {
        state = state.copy(typeWork = value)
    }

    fun onSaveFiles(
        onError: () -> Unit
    ) {
        state = state.copy(isLoad = true)
        viewModelScope.launch {
            errorHandler.makeRequest(request = {
                portfolioRepository.saveFilePortfolio(
                    type = state.type!!,
                    eventName = state.name,
                    eventStartDate = state.date,
                    eventEndDate = state.endDate,
                    eventStatus = state.status,
                    eventPlace = state.place,
                    coauthorsText = state.coathor,
                    workName = when (state.type) {
                        StudentPortfolioType.EXHIBITION -> {
                            state.exhibit
                        }

                        StudentPortfolioType.WORK, StudentPortfolioType.REVIEWS, StudentPortfolioType.THEME, StudentPortfolioType.SCIENCEWORK -> {
                            state.name
                        }

                        else -> {
                            state.theme
                        }
                    },
                    result = state.result,
                    workType = state.typeWork,
                    files = state.attachments
                )
            })

                .doOnSuccess {
                    closeAddedBottomSheet()
                    refresh()
                }
                .doOnError {
                    state = state.copy(isLoad = false)
                    onError()
                }

        }
    }

    private fun refresh() {
        state = state.copy(isLoad = true, listFiles = emptyList())
        viewModelScope.launch {
            val type = state.type ?: return@launch
            errorHandler.makeRequest(request = {
                portfolioRepository.getFilesStudentPortfolioByType(type) })
                .doOnSuccess {
                    withContext(Dispatchers.Main) {
                        state = state.copy(listFiles = it, isLoad = false)
                    }
                }
        }
    }

    fun isEnabledDoneButton(): Boolean {
        return when (state.type) {
            StudentPortfolioType.AWARD, StudentPortfolioType.ACHIEVEMENT -> state.date != null && state.name.isNotBlank() && state.status.isNotBlank()
            StudentPortfolioType.CONFERENCE -> state.name.isNotBlank() && state.theme.isNotBlank() && state.coathor.isNotBlank() && state.place.isNotBlank() && state.date != null && state.endDate != null && state.status.isNotBlank()
            StudentPortfolioType.CONTEST -> state.name.isNotBlank() && state.date != null && state.endDate != null && state.status.isNotBlank() && state.result.isNotEmpty() && state.place.isNotBlank()
            StudentPortfolioType.EXHIBITION -> state.name.isNotBlank() && state.date != null && state.endDate != null && state.exhibit.isNotBlank() && state.place.isNotBlank()
            StudentPortfolioType.SCIENCEREPORT -> state.name.isNotBlank()
            StudentPortfolioType.WORK -> state.name.isNotBlank() && state.typeWork.isNotBlank()
            StudentPortfolioType.TRAINEESHIP -> state.name.isNotBlank() && state.date != null && state.endDate != null && state.place.isNotBlank()
            StudentPortfolioType.REVIEWS -> state.name.isNotBlank() && state.typeWork.isNotBlank()
            StudentPortfolioType.THEME -> state.name.isNotBlank()
            StudentPortfolioType.SCIENCEWORK -> state.name.isNotBlank() && state.typeWork.isNotBlank() && state.coathor.isNotBlank()
            null -> false
        } && state.attachments.isNotEmpty()
    }

    fun openAddedBottomSheet() {
        state = state.copy(openAddPortfolioBottomSheet = true)
    }

    fun closeAddedBottomSheet() {
        state = state.copy(
            openAddPortfolioBottomSheet = false,
            attachments = emptyList(),
            name = "",
            date = null,
            status = "",
            theme = "",
            coathor = "",
            place = "",
            endDate = null,
            result = "",
            exhibit = "",
            typeWork = ""
        )
    }
}