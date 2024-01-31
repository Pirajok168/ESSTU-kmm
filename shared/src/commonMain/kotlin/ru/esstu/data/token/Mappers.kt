package ru.esstu.data.token

import ru.esstu.auth.data.api.model.Tokens
import ru.esstu.auth.domain.model.Token
import ru.esstu.auth.domain.model.TokenOwners
import ru.esstu.data.token.model.TokenPair


fun Tokens.toToken(): Token? {
    return Token(
        type = tokenType,
        access = accessToken,
        refresh = refreshToken,
        owner = when (userType) {
            "STUDENT" -> TokenOwners.Student("${userType.lowercase().take(1)}$userId")
            "EMPLOYEE" -> TokenOwners.Teacher("${userType.lowercase().take(1)}$userId")
            else -> return null
        },
        expiresIn = expiresIn
    )
}

fun Token.toTokenPair() = TokenPair(
    refreshToken = refresh,
    accessToken = access,
    userType = owner.asString(),
    tokenType = type,
    expiresIn = expiresIn
)

fun TokenPair.toToken() = Token(
    access = accessToken,
    refresh = refreshToken,
    owner = TokenOwners.fromString(userType) ?: throw ClassCastException("unknown token type"),
    type = tokenType,
    expiresIn = expiresIn
)