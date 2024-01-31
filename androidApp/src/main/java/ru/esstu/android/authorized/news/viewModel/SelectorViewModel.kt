package ru.esstu.android.authorized.news.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.sync.Mutex
import ru.esstu.android.authorized.news.events.SelectorScreenEvents
import ru.esstu.android.authorized.news.state.SelectorScreenState


class SelectorViewModel() : ViewModel() {
    var state by mutableStateOf(SelectorScreenState())
        private set


    fun onEvent(event: SelectorScreenEvents) {
        when (event) {
            is SelectorScreenEvents.PassNode -> state =
                state.copy(node = event.node, selected = event.selectedFilterNews)

        }
    }

    private val mutex = Mutex()

}