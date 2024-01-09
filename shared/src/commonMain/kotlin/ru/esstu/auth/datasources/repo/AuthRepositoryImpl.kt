package ru.esstu.auth.datasources.repo

import com.soywiz.klock.DateTime
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.esstu.auth.datasources.api.student_teacher.AuthApi
import ru.esstu.auth.datasources.local.ILoginDataRepository
import ru.esstu.auth.datasources.toToken
import ru.esstu.auth.datasources.toTokenPair
import ru.esstu.auth.entities.Token
import ru.esstu.auth.entities.TokenOwners
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.domain.utill.wrappers.ServerErrors
import ru.esstu.domain.utill.wrappers.doOnSuccess


class AuthRepositoryImpl constructor(
    private val portalApi: AuthApi,
    private val cache: ILoginDataRepository,
) : IAuthRepository {
    private val sharedFlow = MutableSharedFlow<Token?>()

    override val logoutFlow: SharedFlow<Token?> = sharedFlow.asSharedFlow()

    override suspend fun refreshToken(): Response<Token> {
        val token = cache.getToken()?.toToken()

        if (token == null) {
            goToLoginScreen(null)
            return Response.Error(ResponseError(error = ServerErrors.Unauthorized))
        }

        val newToken: Response<Token?>  = when (token.owner) {
            is TokenOwners.Teacher, is TokenOwners.Student -> {
                portalApi.refreshToken(token.refresh).transform { it.toToken() }
            }
            TokenOwners.Entrant -> Response.Error(ResponseError(error= ServerErrors.Unknown))
            TokenOwners.Guest -> Response.Success(Token("", "", "", TokenOwners.Guest, null))
        }

        newToken
            .doOnSuccess {
                it?.let {
                    cache.setToken(it.toTokenPair())
                    it.expiresIn?.let {
                        cache.setExpiresDateToken(DateTime.nowUnixLong().plus(it))
                    }
                }
            }


        return when(newToken){
            is Response.Error -> Response.Error(newToken.error)
            is Response.Success -> newToken.data?.let { Response.Success(it) } ?: Response.Error(ResponseError(error = ServerErrors.Unknown))
        }
    }


    override suspend fun auth(login: String, Password: String): Response<Token> {
        val response = portalApi.auth(login, Password)
        val newToken = response.transform {
            it.toToken()
        }.doOnSuccess {
            it?.let {
                cache.setToken(it.toTokenPair())
                it.expiresIn?.let {
                    cache.setExpiresDateToken(DateTime.nowUnixLong().plus(it))
                }
            }
        }
        return when (response){
            is Response.Error -> Response.Error(response.error)
            is Response.Success -> {
                newToken.data?.let { Response.Success(it) } ?: Response.Error(ResponseError(message = "unsupported user type"))
            }
        }

    }

    override suspend fun entrantAuth(login: String, Password: String): Response<Token> {
        TODO("Not yet implemented")
    }

    private suspend fun goToLoginScreen(token: Token? = null) = sharedFlow.emit(token)

    override suspend fun <T> provideToken(call: suspend (type: String, token: String) -> T): Response<T> =
        provideToken { token -> call(token.type, token.access) }


    override suspend fun <T> provideToken(call: suspend (token: Token) -> T): Response<T> {
        suspend fun requestCall(token: Token): Response<T> {
            return try {
                Response.Success(call(token))
            } catch (e1: IOException) {

                e1.printStackTrace()
                Response.Error(ResponseError(message = e1.message))
            }
        }

        val token = cache.getToken()?.toToken()



        if (token == null) {
            goToLoginScreen(null)
            return Response.Error(ResponseError(message = "unauthorized"))
        }

        return when (val result: Response<T> = requestCall(token)) {
            is Response.Error -> {
                val errorCode = result.error.code
                if (errorCode != 401) {
                    result
                } else
                    when (val newToken = refreshToken()) {
                        is Response.Error -> {
                            val newErrorCode = newToken.error.code
                            if (newErrorCode == 401){
                                goToLoginScreen(token)
                            }

                            result
                        }
                        is Response.Success -> {
                            cache.setToken(newToken.data.toTokenPair())
                            requestCall(newToken.data)
                        }
                    }
            }
            is Response.Success -> result
        }
    }


    override suspend fun guestAuth(): Response<Token> {
        val newToken = Token("", "", "", TokenOwners.Guest, null)
        return Response.Success(newToken)
    }

}