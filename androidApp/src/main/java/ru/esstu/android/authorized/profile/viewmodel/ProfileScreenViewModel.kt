package ru.esstu.android.authorized.profile.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance
import ru.esstu.android.authorized.profile.state.ProfileScreenState
import ru.esstu.data.web.api.model.FlowResponse
import ru.esstu.features.profile.main_profile.di.profileDi
import ru.esstu.features.profile.main_profile.domain.repository.IProfileRepository


class ProfileScreenViewModel : ViewModel() {
    private val di: DI by lazy { profileDi() }

    private val profileRepository: IProfileRepository by di.instance<IProfileRepository>()
    var state by mutableStateOf(ProfileScreenState())
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.getProfile()
                .collect {
                    when (it) {
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