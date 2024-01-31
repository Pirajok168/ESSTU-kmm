package ru.esstu.features.account.di

import org.kodein.di.DI
import ru.esstu.di.sharedDi
import ru.esstu.features.account.domain.di.accountDomainModule


fun accountDi() = DI {
    extend(sharedDi)
    importOnce(accountDomainModule())
}