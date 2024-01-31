package ru.esstu.features.messanger.conversations.di

import org.kodein.di.DI
import ru.esstu.di.sharedDi
import ru.esstu.features.messanger.conversations.domain.di.conversationsDomainModule


fun conversationsChatDi() = DI {
    extend(sharedDi)
    importOnce(conversationsDomainModule())
}