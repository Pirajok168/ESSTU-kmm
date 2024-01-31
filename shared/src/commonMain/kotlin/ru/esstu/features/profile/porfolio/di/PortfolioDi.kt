package ru.esstu.features.profile.porfolio.di

import org.kodein.di.DI
import ru.esstu.di.sharedDi
import ru.esstu.features.profile.main_profile.domain.di.profileDomainModule
import ru.esstu.features.profile.porfolio.domain.di.portfolioDomainModule


fun portfolioDi() = DI {
    extend(sharedDi)
    importOnce(profileDomainModule())
    importOnce(portfolioDomainModule())
}