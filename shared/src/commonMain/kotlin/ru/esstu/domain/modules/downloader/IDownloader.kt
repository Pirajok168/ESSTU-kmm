package ru.esstu.domain.modules.downloader

import kotlinx.coroutines.flow.Flow

interface IDownloader{
    suspend fun  downloadFile(url: String): Flow<DownloadResult>
}
