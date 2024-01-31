package ru.esstu.android.authorized.news.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ru.esstu.android.authorized.news.events.MainScreenEvents
import ru.esstu.android.authorized.news.state.MainScreenState

class MainScreenViewModel : ViewModel() {
    var state by mutableStateOf(MainScreenState())
        private set


    fun onEvent(event: MainScreenEvents) {
        when (event) {
            is MainScreenEvents.ChooseOtherFilter -> {
                state = state.copy(selected = event.category)
            }
        }
    }
}