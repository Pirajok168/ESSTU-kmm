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


class SelectorViewModel(
    private val dowloader: IDownloader = ESSTUSdk.accountModule.download
) : ViewModel() {
    var state by mutableStateOf(SelectorScreenState())
        private set


    fun onEvent(event: SelectorScreenEvents) {
        when (event) {
            is SelectorScreenEvents.PassNode -> state =
                state.copy(node = event.node, selected = event.selectedFilterNews)

            is SelectorScreenEvents.LoadFile -> downloadFile(event.uri)
        }
    }

    private val mutex = Mutex()

    private fun downloadFile(file: AttachmentNews) {
        if (file.ext == null) return
        viewModelScope.launch(Dispatchers.IO) {
            dowloader.downloadFile(file.fileUri, file.name ?: "", file.ext.orEmpty())
                .collectLatest { response ->
                    when (response) {
                        is FlowResponse.Error -> {}
                        is FlowResponse.Loading -> {
                            val attachments = state.node?.attachments ?: emptyList()
                            mutex.withLock {
                                state = state.copy(
                                    node = state.node?.copy(attachments = attachments.map {
                                        if (it.fileUri == file.fileUri) {
                                            it.copy(loadProgress = response.progress)
                                        } else {
                                            it
                                        }
                                    })
                                )
                            }

                        }
                        is FlowResponse.Success -> {
                            val attachments = state.node?.attachments ?: emptyList()
                            mutex.withLock {
                                state = state.copy(
                                    node = state.node?.copy(attachments = attachments.map {
                                        if (it.fileUri == file.fileUri) {
                                            it.copy(localFileUri = response.localUri)
                                        } else {
                                            it
                                        }
                                    })
                                )
                            }

                        }
                    }
                }
        }
    }
}