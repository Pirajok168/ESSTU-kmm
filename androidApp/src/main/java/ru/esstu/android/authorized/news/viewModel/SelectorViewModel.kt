package ru.esstu.android.authorized.news.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.esstu.ESSTUSdk
import ru.esstu.android.authorized.news.events.SelectorScreenEvents
import ru.esstu.android.authorized.news.state.SelectorScreenState
import ru.esstu.domain.modules.account.di.accountModule
import ru.esstu.domain.modules.downloader.IDownloader
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.student.news.entities.AttachmentNews


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