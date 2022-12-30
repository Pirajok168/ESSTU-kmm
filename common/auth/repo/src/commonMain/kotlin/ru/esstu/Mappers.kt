package ru.esstu


import ru.esstu.entities.Token
import ru.esstu.entities.TokenOwners
import ru.esstu.entities.TokenPair
import ru.esstu.student_teacher.entities.Tokens

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
        }
    )
}

fun Token.toTokenPair() = TokenPair(
    refreshToken = refresh,
    accessToken = access,
    userType = owner.asString(),
    tokenType = type
)

fun TokenPair.toToken() = Token(
    access = accessToken,
    refresh = refreshToken,
    owner = TokenOwners.fromString(userType) ?: throw ClassCastException("unknown token type"),
    type = tokenType
)