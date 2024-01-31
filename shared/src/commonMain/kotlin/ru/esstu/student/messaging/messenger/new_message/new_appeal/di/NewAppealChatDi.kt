package ru.esstu.student.messaging.messenger.new_message.new_appeal.di

import org.kodein.di.DI
import ru.esstu.di.sharedDi
import ru.esstu.student.messaging.messenger.new_message.new_appeal.domain.di.newAppealDomainModule


fun newAppealChatDi() = DI {
    extend(sharedDi)
    importOnce(newAppealDomainModule())
}