package ru.esstu.android.student.messaging.messenger.supports.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.esstu.ESSTUSdk
import ru.esstu.domain.utill.paginator.Paginator
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.supports.datasource.repo.ISupportsApiRepository
import ru.esstu.student.messaging.messenger.supports.datasource.repo.ISupportsDbRepository
import ru.esstu.student.messaging.messenger.supports.di.supportsModule

data class SupportState(
    val items: List<ConversationPreview> = emptyList(),
    val pageSize: Int = 10,
    val isLoading: Boolean = false,
    val isEndReached: Boolean = false,
    val error: ResponseError? = null,
    val cleanCacheOnRefresh: Boolean = true
)

sealed class SupportEvents {
    object GetNext : SupportEvents()
    object Reload : SupportEvents()
}


class SupportViewModel  constructor(
    supportApi: ISupportsApiRepository = ESSTUSdk.supportsModule.apiRepo,
    supportDb: ISupportsDbRepository = ESSTUSdk.supportsModule.dbRepo
    /*
    updates: ISupportsUpdateRepository*/
) : ViewModel() {
    var supportState by mutableStateOf(SupportState())
        private set

    private val paginator = Paginator(
        initialKey = 0,
        onReset = { if (supportState.cleanCacheOnRefresh) supportDb.clear() },
        onLoad = { supportState = supportState.copy(isLoading = it) },
        onRequest = { key ->
            val cachedConversations = supportDb.getSupports(supportState.pageSize, key)

            if (cachedConversations.isEmpty()) {
                val loadedConversations = supportApi.getSupports(supportState.pageSize, key)

                if (loadedConversations is Response.Success)
                    supportDb.setSupports(loadedConversations.data)

                loadedConversations
            } else
                Response.Success(cachedConversations)
        },
        getNextKey = { key, _ -> key + supportState.pageSize },
        onError = { supportState = supportState.copy(error = it) },

        onRefresh = { items ->
            supportState = supportState.copy(items = items, error = null, isEndReached = items.isEmpty())
        },
        onNext = { _, items ->
            supportState = supportState.copy(items = supportState.items + items, error = null, isEndReached = items.isEmpty())
        }
    )

    init {
       /* viewModelScope.launch {
            updates.updatesFlow.collectLatest {
                if (it is Response.Success && it.data.isNotEmpty()) {
                    supportState = supportState.copy(cleanCacheOnRefresh = true)
                    paginator.refresh()
                }
            }
        }*/
    }

    fun onEvent(event: SupportEvents) {
        when (event) {
            is SupportEvents.GetNext -> viewModelScope.launch { paginator.loadNext() }
            SupportEvents.Reload -> viewModelScope.launch {
                supportState = supportState.copy(cleanCacheOnRefresh = false)
                paginator.refresh()
            }
        }
    }
}


