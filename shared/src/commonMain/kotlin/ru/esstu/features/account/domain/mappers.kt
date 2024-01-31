package ru.esstu.features.account.domain

import ru.esstu.auth.domain.model.Token
import ru.esstu.data.datastore.model.UserEntity
import ru.esstu.features.account.data.model.UserResponse
import ru.esstu.student.messaging.entities.Sender

fun UserResponse.toUser(): Sender? {
    return Sender(
        id = user.id ?: return null,
        summary = user.information ?: return null,
        photo = user.photo,
        lastName = user.lastName.orEmpty(),
        firstName = user.firstName ?: return null,
        patronymic = user.patronymic.orEmpty()
    )
}

fun UserEntity.toUser() = Sender(
    patronymic = patronymic,
    firstName = firstName,
    lastName = lastName,
    photo = photo,
    summary = summary,
    id = id
)

fun Sender.toUserInfoEntity(token: Token) = UserEntity(
    id = id,
    summary = summary,
    photo = photo,
    lastName = lastName,
    firstName = firstName,
    patronymic = patronymic,
    refreshToken = token.refresh
)