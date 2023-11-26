package ru.esstu.android.authorized.student.profile.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.esstu.ESSTUSdk
import ru.esstu.android.authorized.student.profile.state.StudentProfileScreenState
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.student.profile.student.main_profile.domain.di.profileDIModule
import ru.esstu.student.profile.student.main_profile.domain.repository.IProfileRepository
import ru.esstu.student.profile.student.main_profile.domain.repository.ProfileRepositoryImpl


class StudentProfileScreenViewModel(

): ViewModel() {
   private val profileRepository: IProfileRepository = ESSTUSdk.profileDIModule.repo
    var state by mutableStateOf(StudentProfileScreenState())
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.getProfile()
                .collect{
                    when(it){
                        is FlowResponse.Error -> {

                        }
                        is FlowResponse.Loading -> {}
                        is FlowResponse.Success -> {
                            state = state.copy(studentInfo = it.data)
                        }
                    }
                }
        }
    }
}