package ru.esstu.features.profile.main_profile.data.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun profileDataModule() = DI.Module("profileDataModule") {
    bind<ru.esstu.features.profile.main_profile.data.api.ProfileApi>() with singleton {
        ru.esstu.features.profile.main_profile.data.api.ProfileApiImpl(
            authorizedApi = instance()
        )
    }
}