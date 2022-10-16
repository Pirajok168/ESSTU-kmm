package ru.esstu.student.messaging.messenger.appeals.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.student.messaging.messenger.appeals.datasources.api.AppealsApi
import ru.esstu.student.messaging.messenger.appeals.datasources.api.AppealsApiImpl
import ru.esstu.student.messaging.messenger.appeals.datasources.repo.AppealsApiRepositoryImpl
import ru.esstu.student.messaging.messenger.appeals.datasources.repo.IAppealsApiRepository
import kotlin.native.concurrent.ThreadLocal

internal val appealsModule = DI.Module("AppealsModule"){
    bind<AppealsApi>() with singleton {
        AppealsApiImpl(
            portalApi = instance()
        )
    }

    bind<IAppealsApiRepository>() with singleton {
        AppealsApiRepositoryImpl(
            auth = instance(),
            api = instance()
        )
    }
}

@ThreadLocal
object AppealsModule {
    val repo: IAppealsApiRepository
        get() = ESSTUSdk.di.instance()
}

val ESSTUSdk.appealsModule:AppealsModule
    get() = AppealsModule