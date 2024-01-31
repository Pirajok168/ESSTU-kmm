package ru.esstu.student.messaging.dialog_chat.di

import org.kodein.di.DI
import ru.esstu.di.sharedDi
import ru.esstu.student.messaging.dialog_chat.domain.di.dialogChatDomainModule


fun dialogChatDi() = DI {
    extend(sharedDi)
    importOnce(dialogChatDomainModule())
}