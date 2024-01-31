package ru.esstu.student.messaging.group_chat.di

import org.kodein.di.DI
import ru.esstu.di.sharedDi
import ru.esstu.features.messanger.conversations.data.di.conversationsDataModule
import ru.esstu.features.messanger.conversations.domain.di.conversationsDomainModule
import ru.esstu.features.messanger.dialogs.domain.di.dialogsDomainModule
import ru.esstu.student.messaging.group_chat.domain.di.groupChatDomainModule


fun groupChatDi() = DI {
    extend(sharedDi)
    import(conversationsDataModule())
    importOnce(groupChatDomainModule())
}

fun messangerDi() = DI {
    extend(sharedDi)
    import(dialogsDomainModule())
    importOnce(conversationsDomainModule())
}