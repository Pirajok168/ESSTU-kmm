package ru.esstu.data.web.di

import com.russhwolf.settings.Settings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
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
import ru.esstu.ESSTUSdk
import ru.esstu.data.token.repository.LoginDataRepositoryImpl
import ru.esstu.data.web.api.AuthorizedApi
import ru.esstu.data.web.api.HttpEngineFactory
import ru.esstu.data.web.api.UnauthorizedApi
import ru.esstu.data.web.handleError.ErrorHandler
import ru.esstu.data.web.handleError.ErrorProcessor
import ru.esstu.data.web.handleError.ErrorProcessorImpl
import ru.esstu.debugBuild
import kotlin.native.concurrent.ThreadLocal


internal val domainApi = DI.Module(
    name = "DomainApi",
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

        bind<LoginDataRepositoryImpl>() with singleton {
            LoginDataRepositoryImpl(
                authDataStore = Settings()
            )
        }

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

                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            Napier.v("HTTP Client", null, message)
                        }
                    }
                    level = LogLevel.HEADERS
                }
                install(HttpTimeout) {
                    requestTimeoutMillis = 10000
                    socketTimeoutMillis = 10000
                    connectTimeoutMillis = 10000
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
                    loginDataRepository = instance<LoginDataRepositoryImpl>(),
                    client = { instance() },
                    json = Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    },
                    portalApi = instance()
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