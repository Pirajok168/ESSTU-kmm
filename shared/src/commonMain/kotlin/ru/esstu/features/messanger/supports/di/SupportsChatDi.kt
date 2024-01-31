package ru.esstu.features.messanger.supports.di

import org.kodein.di.DI
import ru.esstu.di.sharedDi
import ru.esstu.features.messanger.supports.domain.di.supportsDomainModule


fun supportsChatDi() = DI {
    extend(sharedDi)
    importOnce(supportsDomainModule())
}