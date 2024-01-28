package ru.esstu.student.profile.di

import org.kodein.di.DI
import ru.esstu.student.profile.main_profile.domain.di.profileDIModule


internal val featureModuleProfile = DI.Module("FeatureModuleProfile") {
    importAll(
        profileDIModule,
    )
}