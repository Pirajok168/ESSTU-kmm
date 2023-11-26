package ru.esstu.student.profile.student.porfolio.achivement.di

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
import kotlin.native.concurrent.ThreadLocal


internal val achievementDIModule = DI.Module("AchievementDIModule"){
    bind<AchievementApi>() with singleton {
        AchievementApiImpl(
            instance()
        )
    }

    bind<IAchievementsRepository>() with singleton {
        AchievementsRepositoryImpl(
            instance(),
            instance()
        )
    }
    bind<IAchievementsUpdateRepository>() with singleton {
        AchievementsUpdateRepositoryImpl(
            instance(),
            instance()
        )
    }
}

@ThreadLocal
object AchievementDIModule {
    val repo: IProfileRepository
        get() = ESSTUSdk.di.instance()

}

val ESSTUSdk.achievementDIModule: AchievementDIModule
    get() = AchievementDIModule