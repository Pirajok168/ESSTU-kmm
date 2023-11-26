package ru.esstu.student.profile.student.porfolio.conference.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.student.profile.student.main_profile.domain.repository.IProfileRepository
import ru.esstu.student.profile.student.porfolio.achivement.data.api.AchievementApi
import ru.esstu.student.profile.student.porfolio.achivement.data.api.AchievementApiImpl
import ru.esstu.student.profile.student.porfolio.achivement.domain.repository.AchievementsRepositoryImpl
import ru.esstu.student.profile.student.porfolio.achivement.domain.repository.AchievementsUpdateRepositoryImpl
import ru.esstu.student.profile.student.porfolio.achivement.domain.repository.IAchievementsRepository
import ru.esstu.student.profile.student.porfolio.achivement.domain.repository.IAchievementsUpdateRepository
import ru.esstu.student.profile.student.porfolio.awards.domain.repository.AwardsRepositoryImpl
import ru.esstu.student.profile.student.porfolio.awards.domain.repository.IAwardsRepository
import ru.esstu.student.profile.student.porfolio.conference.data.api.ConferenceApi
import ru.esstu.student.profile.student.porfolio.conference.data.api.ConferenceApiImpl
import ru.esstu.student.profile.student.porfolio.conference.domain.repository.ConferencesRepositoryImpl
import ru.esstu.student.profile.student.porfolio.conference.domain.repository.IConferencesRepository
import kotlin.native.concurrent.ThreadLocal


internal val conferenceDIModule = DI.Module("ConferenceDIModule"){
    bind<ConferenceApi>() with singleton {
        ConferenceApiImpl(
            instance()
        )
    }

    bind<IConferencesRepository>() with singleton {
        ConferencesRepositoryImpl(
            instance(),
            instance()
        )
    }
}

@ThreadLocal
object ConferenceDIModule {
    val repo: IAwardsRepository
        get() = ESSTUSdk.di.instance()

}

val ESSTUSdk.conferenceDIModule: ConferenceDIModule
    get() = ConferenceDIModule