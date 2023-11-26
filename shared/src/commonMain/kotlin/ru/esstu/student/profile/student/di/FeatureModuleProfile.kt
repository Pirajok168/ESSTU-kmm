package ru.esstu.student.profile.student.di

import org.kodein.di.DI
import ru.esstu.student.profile.student.main_profile.domain.di.profileDIModule
import ru.esstu.student.profile.student.porfolio.achivement.di.achievementDIModule


internal val featureModuleProfile = DI.Module("FeatureModuleProfile") {
    importAll(
        profileDIModule,
    )
}