package ru.esstu.features.profile.main_profile.domain.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.features.profile.main_profile.data.di.profileDataModule
import ru.esstu.features.profile.main_profile.domain.repository.IProfileRepository
import ru.esstu.features.profile.main_profile.domain.repository.ProfileRepositoryImpl


fun profileDomainModule() = DI.Module("profileDomainModule") {
    importOnce(profileDataModule())

    bind<IProfileRepository>() with singleton {
        ProfileRepositoryImpl(
            api = instance(),
            auth = instance()
        )
    }
}