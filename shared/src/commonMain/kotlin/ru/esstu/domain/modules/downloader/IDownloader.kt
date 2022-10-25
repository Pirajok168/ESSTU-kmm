package ru.esstu.domain.modules.downloader

import kotlinx.coroutines.flow.Flow
import ru.esstu.domain.utill.wrappers.FlowResponse

interface IDownloader{
    suspend fun  downloadFile(url: String):  Flow<FlowResponse<Int>>
}
