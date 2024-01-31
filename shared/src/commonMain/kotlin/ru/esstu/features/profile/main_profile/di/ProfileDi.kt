package ru.esstu.features.profile.main_profile.di

import org.kodein.di.DI
import ru.esstu.di.sharedDi
import ru.esstu.features.profile.main_profile.domain.di.profileDomainModule


fun profileDi() = DI {
    extend(sharedDi)
    importOnce(profileDomainModule())
}