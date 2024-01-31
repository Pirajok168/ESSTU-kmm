package ru.esstu.student.messaging.messenger.new_message.new_support.di

import org.kodein.di.DI
import ru.esstu.di.sharedDi
import ru.esstu.student.messaging.messenger.new_message.new_support.domain.di.newSupportDomainModule


fun newSupportChatDi() = DI {
    extend(sharedDi)
    importOnce(newSupportDomainModule())
}