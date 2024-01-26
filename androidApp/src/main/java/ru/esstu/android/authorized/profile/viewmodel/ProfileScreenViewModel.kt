package ru.esstu.android.authorized.profile.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.esstu.ESSTUSdk
import ru.esstu.android.authorized.profile.state.ProfileScreenState
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.student.profile.student.main_profile.domain.di.profileDIModule
import ru.esstu.student.profile.student.main_profile.domain.repository.IProfileRepository


class ProfileScreenViewModel(

): ViewModel() {
   private val profileRepository: IProfileRepository = ESSTUSdk.profileDIModule.repo
    var state by mutableStateOf(ProfileScreenState())
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
                            state = state.copy(profileInfo = it.data)
                        }
                    }
                }
        }
    }
}