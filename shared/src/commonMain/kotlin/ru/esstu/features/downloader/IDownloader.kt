package ru.esstu.features.downloader

import kotlinx.coroutines.flow.Flow
import ru.esstu.data.web.api.model.FlowResponse

interface IDownloader {
    suspend fun downloadFile(url: String, name: String, ext: String): Flow<FlowResponse<Int>>
}
