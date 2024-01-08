package ru.esstu.domain.ktor

import com.russhwolf.settings.Settings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.statement.HttpResponse
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.auth.datasources.local.TokenDSManagerImpl
import ru.esstu.debugBuild

@Serializable
data class Error(val code: Int, val message: String)

class CustomResponseException(response: HttpResponse, message: String) :
    ResponseException(response, message) {
    override val message: String = message
}


internal val domainApi = DI.Module(
    name =  "DomainApi",
    init = {
        bind<TokenDSManagerImpl>() with  singleton { TokenDSManagerImpl(
            authDataStore = Settings()
        ) }

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
//                install(UserAgent) {
//                    agent = "Mobile client"
//                }

                install(Logging){
                    logger = object : Logger{
                        override fun log(message: String) {
                            Napier.v("HTTP Client", null, message)
                        }
                    }
                    level = LogLevel.HEADERS
                }

/*                install(HttpTimeout) {
                    requestTimeoutMillis = Long.MAX_VALUE
                    socketTimeoutMillis = Long.MAX_VALUE
                    connectTimeoutMillis = Long.MAX_VALUE
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

                */

                defaultRequest {
                    host = "esstu.ru"
                    url {
                        protocol = URLProtocol.HTTPS
                    }
                }
            }.also { Napier.base(DebugAntilog()) }
        }

        bind {
            singleton {
                UnauthorizedApi(
                    client = { instance() },
                    json = Json
                )
            }
        }

        bind {
            singleton {
                AuthorizedApi(
                    loginDataRepository = instance<TokenDSManagerImpl>(),
                    client = { instance() },
                    json = Json
                )
            }
        }

    }
)