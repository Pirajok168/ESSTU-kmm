package ru.esstu.domain.modules.downloader

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.FileSystem
import okio.Path.Companion.toPath
import ru.esstu.domain.modules.account.datasources.datastore.producePath
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.wrappers.ResponseError





class Downloader(
    private val fileSystem: FileSystem,
    private val httpClient: HttpClient
): IDownloader {
    @OptIn(InternalAPI::class)
    override suspend fun downloadFile(url: String): Flow<FlowResponse<Int>> = flow{
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

            }while (currentRead > 0)

            if (response.status.isSuccess()) {



                fileSystem.write("${producePath().path}/file".toPath(), true){
                    write(data)
                }


                emit(FlowResponse.Success(100))
            } else {
                emit(FlowResponse.Error(ResponseError(message = "Ошибка скачивания")))
            }
        }catch (e: TimeoutCancellationException){
            emit(FlowResponse.Error(ResponseError(message = "Сервер не отвечает")))
        }catch (t: Throwable){
            emit(FlowResponse.Error(ResponseError(message = "Ошибка скачивания")))
        }


    }
}