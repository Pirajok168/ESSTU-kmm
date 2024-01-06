package ru.esstu.android.authorized.student.profile.portfolio.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.esstu.ESSTUSdk
import ru.esstu.android.authorized.student.profile.portfolio.state.PortfolioState
import ru.esstu.domain.utill.wrappers.doOnError
import ru.esstu.domain.utill.wrappers.doOnSuccess
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.profile.student.porfolio.domain.di.portfolioModule
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioType
import ru.esstu.student.profile.student.porfolio.domain.repository.IPortfolioRepository

class PortfolioViewModel: ViewModel() {

    private val portfolioRepository: IPortfolioRepository = ESSTUSdk.portfolioModule.repo

    var state by mutableStateOf(PortfolioState())
        private set

    fun preDisplayFile(type: PortfolioType){
        state = state.copy(type = type, listFiles = emptyList(), isLoad = true)
        viewModelScope.launch {
            portfolioRepository.getFilesPortfolioByType(type)
                .doOnSuccess {
                    withContext(Dispatchers.Main){
                        state = state.copy(listFiles = it, isLoad = false)
                    }
                }
        }
    }

    fun onPassAttachments(attachments: List<CachedFile>){
        state = state.copy(attachments = state.attachments + attachments)
    }

    fun onRemoveAttachment(file: CachedFile){
        state = state.copy(attachments = state.attachments.filterNot { it == file  })
    }

    fun onSetDate(value: Long){
        state = state.copy(date = value)
    }
    fun onSetTheme(value: String){
        state = state.copy(theme = value)
    }
    fun onSetStatus(value: String){
        state = state.copy(status = value)
    }
    fun onInputName(value: String){
        state = state.copy(name = value)
    }

    fun onInputExhibit(value: String){
        state = state.copy(exhibit = value)
    }
    fun onInputPlace(value: String) {
        state = state.copy(place = value)
    }

    fun onInputCoathor(value: String){
        state = state.copy(coathor = value)
    }

    fun onInputStartDate(value: Long){
        state = state.copy(date = value)
    }

    fun onInputResult(value: String){
        state = state.copy(result = value)
    }
    fun onInputEndDate(value: Long){
        state = state.copy(endDate = value)
    }

    fun onInputTypeWork(value: String){
        state = state.copy(typeWork = value)
    }

    fun onSaveFiles(
        onError: () -> Unit
    ){
        state = state.copy(isLoad = true)
        viewModelScope.launch {
            portfolioRepository.saveFilePortfolio(
                type = state.type!!,
                eventName = state.name,
                eventStartDate = state.date,
                eventEndDate = state.endDate,
                eventStatus = state.status,
                eventPlace = state.place,
                coauthorsText = state.coathor,
                workName = when (state.type) {
                    PortfolioType.EXHIBITION -> {
                        state.exhibit
                    }
                    PortfolioType.WORK, PortfolioType.REVIEWS, PortfolioType.THEME, PortfolioType.SCIENCEWORK -> {
                        state.name
                    }
                    else -> {
                        state.theme
                    }
                },
                result = state.result,
                workType = state.typeWork,
            )
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

    private fun refresh(){
        state = state.copy(isLoad = true, listFiles = emptyList())
        viewModelScope.launch {
            portfolioRepository.getFilesPortfolioByType(state.type ?: return@launch)
                .doOnSuccess {
                    withContext(Dispatchers.Main){
                        state = state.copy(listFiles = it, isLoad = false)
                    }
                }
        }
    }
    fun isEnabledDoneButton(): Boolean {
        return when(state.type){
            PortfolioType.AWARD, PortfolioType.ACHIEVEMENT -> state.date != null && state.name.isNotBlank() && state.status.isNotBlank()
            PortfolioType.CONFERENCE -> state.name.isNotBlank() && state.theme.isNotBlank() && state.coathor.isNotBlank() && state.place.isNotBlank() && state.date != null && state.endDate != null && state.status.isNotBlank()
            PortfolioType.CONTEST -> state.name.isNotBlank() && state.date != null && state.endDate != null && state.status.isNotBlank() && state.result.isNotEmpty() && state.place.isNotBlank()
            PortfolioType.EXHIBITION -> state.name.isNotBlank() && state.date != null && state.endDate != null && state.exhibit.isNotBlank() && state.place.isNotBlank()
            PortfolioType.SCIENCEREPORT -> state.name.isNotBlank()
            PortfolioType.WORK -> state.name.isNotBlank() && state.typeWork.isNotBlank()
            PortfolioType.TRAINEESHIP -> state.name.isNotBlank() && state.date != null && state.endDate != null && state.place.isNotBlank()
            PortfolioType.REVIEWS -> state.name.isNotBlank() && state.typeWork.isNotBlank()
            PortfolioType.THEME -> state.name.isNotBlank()
            PortfolioType.SCIENCEWORK -> state.name.isNotBlank() && state.typeWork.isNotBlank() && state.coathor.isNotBlank()
            null -> false
        }
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