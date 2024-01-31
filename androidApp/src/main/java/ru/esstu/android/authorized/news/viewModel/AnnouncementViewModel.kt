package ru.esstu.android.authorized.news.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance
import ru.esstu.android.authorized.news.events.AnnouncementEvents
import ru.esstu.android.authorized.news.state.AnnouncementState
import ru.esstu.data.web.handleError.ErrorHandler
import ru.esstu.domain.utill.paginator.Paginator
import ru.esstu.features.news.announcement.di.announcementDi
import ru.esstu.features.news.announcement.domain.repo.IAnnouncementsRepository

class AnnouncementViewModel : ViewModel() {

    private val di: DI by lazy { announcementDi() }

    private val repo: IAnnouncementsRepository by di.instance<IAnnouncementsRepository>()
    private val errorHandler: ErrorHandler by di.instance<ErrorHandler>()

    var state by mutableStateOf(AnnouncementState())
        private set

    private val paginator = Paginator(
        onReset = { repo.clearCache() },
        initialKey = 0,
        getNextKey = { currentKey, _ -> currentKey + state.pageSize },
        onRequest = { key ->
            errorHandler.makeRequest(
                request =  {
                    repo.getAnnouncementsPage(key, state.pageSize)
                }
            )
        },
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