package ru.esstu.core.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.core.ktor.ApiClient
import ru.esstu.debugBuild
import ru.esstu.domain.ktor.CustomResponseException
import ru.esstu.domain.ktor.HttpEngineFactory

internal val coreModule = DI.Module("coreModule") {
    bind<HttpClient>() with singleton {
        val engine = instance<HttpEngineFactory>().createEngine()
        HttpClient(engine) {

            debugBuild()
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(UserAgent) {
                agent = "Mobile client"
            }

           /* install(Logging){
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.v("HTTP Client", null, message)
                    }
                }
                level = LogLevel.HEADERS
            }*/

            install(HttpTimeout) {
                requestTimeoutMillis = 9000
            }

            HttpResponseValidator {
                validateResponse { response ->
                    val error: Int = response.status.value
                    if (error == 400 || error == 401) {
                        throw CustomResponseException(response, "Неверный логин или пароль")
                    }
                    if(error == 500){
                        throw CustomResponseException(response, "Сервер не работает")
                    }
                }
            }

            defaultRequest {
                host = "esstu.ru"
                url {
                    protocol = URLProtocol.HTTPS
                }
            }
        }
    }

    bind<ApiClient>() with singleton {
        ApiClient {
            instance()
        }
    }
}