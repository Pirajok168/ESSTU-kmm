package ru.esstu.features.account.domain.repo

import kotlinx.coroutines.flow.firstOrNull
import ru.esstu.auth.domain.model.Token
import ru.esstu.auth.domain.model.TokenOwners
import ru.esstu.auth.domain.repo.IAuthRepository
import ru.esstu.data.datastore.IAccountInfoDSManager
import ru.esstu.data.token.repository.ILoginDataRepository
import ru.esstu.data.token.toToken
import ru.esstu.data.web.api.model.Response
import ru.esstu.data.web.api.model.ResponseError
import ru.esstu.data.web.api.model.ServerErrors
import ru.esstu.features.account.data.api.AccountInfoApi
import ru.esstu.features.account.domain.toUser
import ru.esstu.features.account.domain.toUserInfoEntity
import ru.esstu.student.messaging.entities.Sender


class AccountInfoRepositoryImpl(
    private val loginDataRepository: ILoginDataRepository,
    private val userCache: IAccountInfoDSManager,
    private val api: AccountInfoApi,
    private val auth: IAuthRepository
) : IAccountInfoApiRepository {
    override suspend fun clearUser() = userCache.clearUser()

    private suspend fun getCachedUser(token: Token): Pair<TokenOwners, Sender?> {
        val cachedUser = userCache.getUser.firstOrNull()

        return token.owner to
                if (cachedUser != null && cachedUser.refreshToken == token.refresh)
                    cachedUser.toUser()
                else
                    null
    }

    override suspend fun getUser(): Response<Sender> {

        val cache = auth.provideToken { token ->

            val cachedUser = userCache.getUser.firstOrNull()

            token.owner to
                    if (cachedUser != null && cachedUser.refreshToken == token.refresh)
                        cachedUser.toUser()
                    else
                        null

        } as? Response.Success ?: return Response.Error(ResponseError())

        val (owner, cachedUser) = cache.data

        if (cachedUser != null)
            return Response.Success(cachedUser)

        val token = loginDataRepository.getAccessToken()?.toToken() ?: return Response.Error(
            ResponseError(error = ServerErrors.Unauthorized)
        )
        val response = when (owner) {
            TokenOwners.Entrant,
            TokenOwners.Guest -> Response.Error(ResponseError(message = "unsupported user"))

            is TokenOwners.Student ->
                api.getUser(userId = owner.id)
                    .transform {
                        it.toUser()?.toUserInfoEntity(token)
                    }

            is TokenOwners.Teacher ->
                api.getUser(userId = owner.id)
                    .transform {
                        it.toUser()?.toUserInfoEntity(token)
                    }
        }

        return when (response) {
            is Response.Error -> Response.Error(response.error)
            is Response.Success -> when (val user = response.data) {
                null -> Response.Error(ResponseError(message = "Cast error"))
                else -> {
                    userCache.setUser(user)
                    Response.Success(user.toUser())
                }
            }
        }
    }
}