package ru.esstu.core.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.core.ktor.HttpEngineFactory

val coreModule = DI.Module("coreModule") {
    bind<HttpEngineFactory>() with singleton { HttpEngineFactory() }
    bind<HttpClient>() with singleton {
        val engine = instance<HttpEngineFactory>().createEngine()
        HttpClient(engine) {


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


            defaultRequest {
                host = "esstu.ru"
                url {
                    protocol = URLProtocol.HTTPS
                }
            }
        }
    }

    bind<AuthorizedHttpClient>() with singleton {
        AuthorizedHttpClient(

        ) {
            instance()
        }
    }
}