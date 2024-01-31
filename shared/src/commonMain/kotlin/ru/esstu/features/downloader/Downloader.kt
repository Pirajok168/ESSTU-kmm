package ru.esstu.features.downloader

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.contentLength
import io.ktor.http.isSuccess
import io.ktor.util.InternalAPI
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.FileSystem
import okio.Path.Companion.toPath
import ru.esstu.data.fileSystem.producePath
import ru.esstu.data.web.api.model.FlowResponse
import ru.esstu.data.web.api.model.ResponseError


class Downloader(
    private val fileSystem: FileSystem,
    private val httpClient: HttpClient
) : IDownloader {
    @OptIn(InternalAPI::class)
    override suspend fun downloadFile(
        url: String,
        name: String,
        ext: String
    ): Flow<FlowResponse<Int>> = flow {
        try {
            val response = httpClient.get {
                url(url)
            }.call.response
            val data = ByteArray(response.contentLength()!!.toInt())
            var offset = 0

            do {
                val currentRead = response.content.readAvailable(data, offset, data.size)
                offset += currentRead

                val progress = (offset * 100f / data.size)
                emit(FlowResponse.Loading(progress = progress))

            } while (currentRead > 0)

            if (response.status.isSuccess()) {

                val patch = "${producePath().path}/$name.$ext"
                fileSystem.write(patch.toPath(), true) {
                    write(data)
                }

                emit(FlowResponse.Success(100, patch))
            } else {
                emit(FlowResponse.Error(ResponseError(message = "Ошибка скачивания")))
            }
        } catch (e: TimeoutCancellationException) {
            emit(FlowResponse.Error(ResponseError(message = "Сервер не отвечает")))
        } catch (t: Throwable) {
            emit(FlowResponse.Error(ResponseError(message = "Ошибка скачивания")))
        }


    }
}