package ru.esstu.domain.ktor

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.debugBuild
import ru.esstu.domain.utill.wrappers.ResponseError

@Serializable
data class Error(val code: Int, val message: String)

class CustomResponseException(response: HttpResponse, message: String) :
    ResponseException(response, message) {
    override val message: String = message
}


internal val domainApi = DI.Module(
    name =  "DomainApi",
    init = {
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

                install(Logging){
                    logger = Logger.DEFAULT
                    level = LogLevel.HEADERS
                }

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
    }
)