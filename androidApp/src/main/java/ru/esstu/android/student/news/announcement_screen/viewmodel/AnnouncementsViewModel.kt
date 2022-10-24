package ru.esstu.android.student.news.announcement_screen.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.esstu.domain.utill.paginator.Paginator
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.ESSTUSdk
import ru.esstu.domain.modules.account.datasources.datastore.producePath
import ru.esstu.domain.modules.account.di.accountModule
import ru.esstu.domain.modules.downloader.DownloadResult
import ru.esstu.domain.modules.downloader.IDownloader
import ru.esstu.student.news.entities.AttachmentNews
import ru.esstu.student.news.announcement.datasources.repo.IAnnouncementsRepository
import ru.esstu.student.news.announcement.datasources.repo.IAnnouncementsUpdateRepository
import ru.esstu.student.news.announcement.di.announcementsModule
import ru.esstu.student.news.entities.NewsNode
import java.io.OutputStream
import kotlin.math.roundToInt
import kotlin.text.get


data class AnnouncementsState(
    val pages: List<NewsNode> = emptyList(),
    val isPagesLoading: Boolean = false,
    val pageLoadingError: ResponseError? = null,
    val isEndReached:Boolean = false,

    val pageSize: Int = 10
)

sealed class AnnouncementsEvents {
    object LoadAndRefresh:AnnouncementsEvents()
    object LoadNext : AnnouncementsEvents()
    object Refresh : AnnouncementsEvents()
    data class DownloadAttachment(val attachment: AttachmentNews):AnnouncementsEvents()
    data class UpdateAttachment(val announcementId: Long, val attachment: AttachmentNews):AnnouncementsEvents()
}


class AnnouncementsViewModel  constructor(
    private val repo: IAnnouncementsRepository = ESSTUSdk.announcementsModule.repo,
    private val downolad: IDownloader = ESSTUSdk.accountModule.download,
   // private val downloadRepo:IFileDownloadRepository = EngineSDK.announcementsModule.updates,
    updates: IAnnouncementsUpdateRepository = ESSTUSdk.announcementsModule.updates
) : ViewModel() {

    var state by mutableStateOf(AnnouncementsState())
        private set

    fun download(){
        viewModelScope.launch(Dispatchers.IO) {
            downolad.downloadFile("aicstorages/publicDownload/0808e1da0a00001e5568aede250c7f27.pdf").collect{
                Log.e("download", it.toString())
            }
        }


    }


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

    init {
        viewModelScope.launch {
            updates.getUpdates().collectLatest { updates ->
                if (updates.data?.isNotEmpty() == true)
                    paginator.refresh()
            }
        }
    }

    fun onEvent(event: AnnouncementsEvents) {
        when (event) {
            AnnouncementsEvents.LoadAndRefresh ->viewModelScope.launch { paginator.loadNext(); paginator.refresh() }
            AnnouncementsEvents.LoadNext -> viewModelScope.launch { paginator.loadNext() }
            AnnouncementsEvents.Refresh -> viewModelScope.launch { paginator.refresh() }
            //is AnnouncementsEvents.DownloadAttachment -> viewModelScope.launch { downloadRepo.downloadFile(event.attachment) }
            is AnnouncementsEvents.UpdateAttachment -> onUpdateAttachment(event.attachment,event.announcementId)
            else -> {}
        }
    }

    private fun onUpdateAttachment(attachment: AttachmentNews, announcementId: Long) {
        val pages  = state.pages.map { node->
            if(node.id != announcementId) return@map node
            node.copy(
                attachments = node.attachments.map inl@{ att->
                    if(att.id!= attachment.id) return@inl att
                    attachment
                }
            )
        }

        state = state.copy(pages = pages)
    }
}