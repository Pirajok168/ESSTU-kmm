package ru.esstu.domain.modules.downloader

import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okio.FileSystem
import okio.ForwardingSource
import okio.Path
import okio.Path.Companion.toPath
import okio.Source
import ru.esstu.domain.modules.account.datasources.datastore.producePath
import ru.esstu.domain.utill.wrappers.FlowResponse
import kotlin.math.roundToInt



sealed class DownloadResult {
    object Success : DownloadResult()

    data class Error(val message: String, val cause: Exception? = null) : DownloadResult()

    data class Progress(val progress: Int): DownloadResult()
}



class Downloader(
    private val fileSystem: FileSystem,
    private val httpClient: HttpClient
): IDownloader {
    @OptIn(InternalAPI::class)
    override suspend fun downloadFile(url: String): Flow<DownloadResult> = flow{
        try {
            val response = httpClient.get {
                url(url)
            }.call.response
            val data = ByteArray(response.contentLength()!!.toInt())
            var offset = 0

            do {
                val currentRead = response.content.readAvailable(data, offset, data.size)
                offset += currentRead

                val progress = (offset * 100f / data.size).roundToInt()
                emit(DownloadResult.Progress(progress))

            }while (currentRead > 0)

            if (response.status.isSuccess()) {
                //download(data,producePath().path.toPath() )


                fileSystem.write("${producePath().path}/file".toPath(), true){
                    write(data)
                }


                emit(DownloadResult.Success)
            } else {
                emit(DownloadResult.Error("File not downloaded"))
            }
        }catch (e: TimeoutCancellationException){

        }catch (t: Throwable){

        }


    }
}