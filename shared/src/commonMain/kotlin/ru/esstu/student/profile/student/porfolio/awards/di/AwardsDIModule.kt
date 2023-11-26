package ru.esstu.student.profile.student.porfolio.awards.di

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
import kotlin.native.concurrent.ThreadLocal


internal val awardsDIModule = DI.Module("AwardsDIModule"){
    bind<AchievementApi>() with singleton {
        AchievementApiImpl(
            instance()
        )
    }

    bind<IAwardsRepository>() with singleton {
        AwardsRepositoryImpl(
            instance(),
            instance()
        )
    }
}

@ThreadLocal
object AwardsDIModule {
    val repo: IAwardsRepository
        get() = ESSTUSdk.di.instance()

}

val ESSTUSdk.awardDIModule: AwardsDIModule
    get() = AwardsDIModule