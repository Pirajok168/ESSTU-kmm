package ru.esstu.android.authorized.news.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ru.esstu.android.authorized.news.component.FilterNews
import ru.esstu.android.authorized.news.events.SelectorScreenEvents
import ru.esstu.android.authorized.news.state.SelectorScreenState
import ru.esstu.student.news.entities.NewsNode



class SelectorViewModel : ViewModel() {
    var state by mutableStateOf(SelectorScreenState())
        private set


    fun onEvent(event: SelectorScreenEvents) {
        when (event) {
            is SelectorScreenEvents.PassNode -> state = state.copy(node = event.node,selected =event.selectedFilterNews)
        }
    }



}