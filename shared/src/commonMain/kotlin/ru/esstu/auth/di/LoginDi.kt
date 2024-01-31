package ru.esstu.auth.di

import org.kodein.di.DI
import ru.esstu.auth.domain.di.authorizationDomainModule
import ru.esstu.di.sharedDi

fun authorizationDi() = DI {
    extend(sharedDi)
    importOnce(authorizationDomainModule())
}