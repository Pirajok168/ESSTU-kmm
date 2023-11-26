package ru.esstu.student.profile.student.porfolio.di

import org.kodein.di.DI
import ru.esstu.student.profile.student.main_profile.domain.di.profileDIModule
import ru.esstu.student.profile.student.porfolio.achivement.di.achievementDIModule
import ru.esstu.student.profile.student.porfolio.awards.di.awardsDIModule
import ru.esstu.student.profile.student.porfolio.conference.di.conferenceDIModule


internal val featureModulePortfolio = DI.Module("featureModulePortfolio") {
    importAll(
        achievementDIModule,
        awardsDIModule,
        conferenceDIModule
    )
}