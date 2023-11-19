package ru.esstu.android.authorized.news.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.esstu.ESSTUSdk
import ru.esstu.android.authorized.news.events.AnnouncementEvents
import ru.esstu.android.authorized.news.events.MainScreenEvents
import ru.esstu.android.authorized.news.state.MainScreenState
import ru.esstu.student.news.announcement.datasources.repo.IAnnouncementsRepository
import ru.esstu.student.news.announcement.di.announcementsModule

class MainScreenViewModel(
    private val repo: IAnnouncementsRepository = ESSTUSdk.announcementsModule.repo
): ViewModel()  {
    var state by mutableStateOf(MainScreenState())
        private set


    fun onEvent(event: MainScreenEvents) {
        when (event) {
            is MainScreenEvents.ChooseOtherFilter ->{
                state = state.copy(selected = event.category)
            }
        }
    }
}