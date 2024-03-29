package ru.esstu.auth.datasources

import ru.esstu.auth.datasources.api.student_teacher.Tokens
import ru.esstu.auth.datasources.local.TokenPair
import ru.esstu.auth.entities.Token
import ru.esstu.auth.entities.TokenOwners

/*fun EntrantTokens.toToken(): Token = Token(
    type = tokenType,
    access = this.accessToken,
    refresh = this.refreshToken,
    owner = TokenOwners.Entrant
)*/

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