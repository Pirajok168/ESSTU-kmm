package ru.esstu.android.authorized.news.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.esstu.ESSTUSdk
import ru.esstu.android.authorized.news.events.AnnouncementEvents
import ru.esstu.android.authorized.news.state.AnnouncementState
import ru.esstu.domain.utill.paginator.Paginator
import ru.esstu.student.news.announcement.datasources.repo.IAnnouncementsRepository
import ru.esstu.student.news.announcement.di.announcementsModule

class AnnouncementViewModel(
    private val repo: IAnnouncementsRepository = ESSTUSdk.announcementsModule.repo
): ViewModel()  {
    var state by mutableStateOf(AnnouncementState())
        private set

    private val paginator = Paginator(
        onReset = { repo.clearCache() },
        initialKey = 0,
        getNextKey = { currentKey, _ -> currentKey + state.pageSize },
        onRequest = { key -> repo.getAnnouncementsPage(key, state.pageSize) },
        onLoad = { state = state.copy(isPagesLoading = it) },
        onError = { state = state.copy(pageLoadingError = it) },
        onRefresh = { page -> state = state.copy(pages = page, pageLoadingError = null, isEndReached = page.isEmpty()) },
        onNext = { _, page -> state = state.copy(pages = state.pages + page, pageLoadingError = null, isEndReached = page.isEmpty()) }
    )

    fun onEvent(event: AnnouncementEvents) {
        when (event) {
            AnnouncementEvents.LoadAndRefresh ->viewModelScope.launch { paginator.loadNext(); paginator.refresh() }
            AnnouncementEvents.LoadNext -> viewModelScope.launch { paginator.loadNext() }
            AnnouncementEvents.Refresh -> viewModelScope.launch { paginator.refresh() }
        }
    }
}