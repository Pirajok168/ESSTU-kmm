package ru.esstu.domain.ktor

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton


internal val domainApi = DI.Module(
    name =  "DomainApi",
    init = {
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



                defaultRequest {
                    host = "esstu.ru"
                    url {
                        protocol = URLProtocol.HTTPS
                    }

                }
            }
        }
    }
)