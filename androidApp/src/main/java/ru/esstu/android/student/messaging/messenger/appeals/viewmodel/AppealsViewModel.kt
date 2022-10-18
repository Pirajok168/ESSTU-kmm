package ru.esstu.android.student.messaging.messenger.appeals.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.esstu.ESSTUSdk
import ru.esstu.domain.utill.paginator.Paginator
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.student.messaging.messenger.appeals.datasources.repo.IAppealsApiRepository
import ru.esstu.student.messaging.messenger.appeals.di.appealsModule
import ru.esstu.student.messaging.messenger.appeals.entities.Appeals

import ru.esstu.student.messaging.messenger.conversations.entities.Conversation


data class AppealState(
    val items: List<Appeals> = emptyList(),
    val pageSize: Int = 10,
    val isLoading: Boolean = false,
    val isEndReached: Boolean = false,
    val error: ResponseError? = null,
    val cleanCacheOnRefresh: Boolean = true
)

sealed class AppealEvents {
    object GetNext : AppealEvents()
    object Reload : AppealEvents()
}



class AppealsViewModel  constructor(
    appealApi: IAppealsApiRepository = ESSTUSdk.appealsModule.repo,
    //appealDb: IAppealsDbRepository,
  //  updates: IAppealsUpdateRepository
) : ViewModel() {
    var appealsState by mutableStateOf(AppealState())
        private set

    /*private val paginator = Paginator(
        initialKey = 0,
        onReset = { if (appealsState.cleanCacheOnRefresh) appealDb.clear() },
        onLoad = { appealsState = appealsState.copy(isLoading = it) },
        onRequest = { key ->
            val cachedConversations = appealDb.getAppeals(appealsState.pageSize, key)

            if (cachedConversations.isEmpty()) {
                val loadedConversations = appealApi.getSupports(appealsState.pageSize, key)

                if (loadedConversations is Response.Success)
                    appealDb.setAppeals(loadedConversations.data.mapIndexed { index, conversation -> index + key to conversation }.toMap())

                loadedConversations
            } else
                Response.Success(cachedConversations)
        },
        getNextKey = { key, _ -> key + appealsState.pageSize },
        onError = { appealsState = appealsState.copy(error = it) },

        onRefresh = { items ->
            appealsState = appealsState.copy(items = items, error = null, isEndReached = items.isEmpty())
        },
        onNext = { _, items ->
            appealsState = appealsState.copy(items = appealsState.items + items, error = null, isEndReached = items.isEmpty())
        }
    )*/

    init {
        viewModelScope.launch {
            when(val data = appealApi.getSupports(10,0)){
                is Response.Error -> {

                }
                is Response.Success -> {
                    appealsState = appealsState.copy(items = data.data)
                }
            }
        }

       /* viewModelScope.launch {
            updates.updatesFlow.collectLatest {
                if (it is Response.Success && it.data.isNotEmpty()) {
                    appealsState = appealsState.copy(cleanCacheOnRefresh = true)
                    paginator.refresh()
                }
            }
        }*/
    }

    fun onEvent(event: AppealEvents) {
        /*when (event) {
            is AppealEvents.GetNext -> viewModelScope.launch { paginator.loadNext() }
            AppealEvents.Reload -> viewModelScope.launch {
                appealsState = appealsState.copy(cleanCacheOnRefresh = true)
                paginator.refresh()
            }
        }*/
    }
}