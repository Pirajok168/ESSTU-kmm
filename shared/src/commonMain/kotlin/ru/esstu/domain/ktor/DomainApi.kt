package ru.esstu.domain.ktor

import com.russhwolf.settings.Settings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.UserAgent
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
import ru.esstu.ESSTUSdk
import ru.esstu.auth.datasources.local.TokenDSManagerImpl
import ru.esstu.debugBuild
import ru.esstu.domain.handleError.ErrorHandler
import ru.esstu.domain.handleError.ErrorProcessor
import ru.esstu.domain.handleError.ErrorProcessorImpl
import kotlin.native.concurrent.ThreadLocal


internal val domainApi = DI.Module(
    name =  "DomainApi",
    init = {
        bind<ErrorProcessor> {
            singleton {
                ErrorProcessorImpl()
            }
        }

        bind {
            singleton {
                ErrorHandler(instance())
            }
        }

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
                install(UserAgent) {
                    agent = "Mobile client"
                }

                install(Logging){
                    logger = object : Logger{
                        override fun log(message: String) {
                            Napier.v("HTTP Client", null, message)
                        }
                    }
                    level = LogLevel.HEADERS
                }
                install(HttpTimeout) {
                    requestTimeoutMillis = 900000
                    socketTimeoutMillis = 900000
                    connectTimeoutMillis = 900000
                }

                defaultRequest {
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

@ThreadLocal
object ErrorHandlerModule {
    val errorHandler: ErrorHandler
        get() = ESSTUSdk.di.instance()

    val errorProcessor: ErrorProcessor
        get() = ESSTUSdk.di.instance()
}

val ESSTUSdk.domainApi: ErrorHandlerModule
    get() = ErrorHandlerModule