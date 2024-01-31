package ru.esstu.features.messanger.appeal.di

import org.kodein.di.DI
import ru.esstu.di.sharedDi
import ru.esstu.features.messanger.appeal.domain.di.appealDomainModule


fun appealDi() = DI {
    extend(sharedDi)
    importOnce(appealDomainModule())
}