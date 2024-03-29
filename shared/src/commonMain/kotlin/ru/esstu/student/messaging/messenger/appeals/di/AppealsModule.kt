package ru.esstu.student.messaging.messenger.appeals.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.student.domain.db.IDatabaseStudent
import ru.esstu.student.messaging.messenger.appeals.datasources.api.AppealsApi
import ru.esstu.student.messaging.messenger.appeals.datasources.api.AppealsApiImpl
import ru.esstu.student.messaging.messenger.appeals.datasources.db.AppealsCacheDao
import ru.esstu.student.messaging.messenger.appeals.datasources.db.AppealsCacheDatabase
import ru.esstu.student.messaging.messenger.appeals.datasources.db.AppealsTimestampDao
import ru.esstu.student.messaging.messenger.appeals.datasources.db.AppealsTimestampDatabase
import ru.esstu.student.messaging.messenger.appeals.datasources.repo.AppealsApiRepositoryImpl
import ru.esstu.student.messaging.messenger.appeals.datasources.repo.AppealsDbRepositoryImpl
import ru.esstu.student.messaging.messenger.appeals.datasources.repo.AppealsUpdatesRepositoryImpl
import ru.esstu.student.messaging.messenger.appeals.datasources.repo.IAppealUpdatesRepository
import ru.esstu.student.messaging.messenger.appeals.datasources.repo.IAppealsApiRepository
import ru.esstu.student.messaging.messenger.appeals.datasources.repo.IAppealsDbRepository
import kotlin.native.concurrent.ThreadLocal

internal val appealsModule = DI.Module("AppealsModule"){
    bind<AppealsApi>() with singleton {
        AppealsApiImpl(
            instance()
        )
    }

    bind<IAppealsApiRepository>() with singleton {
        AppealsApiRepositoryImpl(
            instance()
        )
    }

    bind<AppealsCacheDao>() with singleton {
        AppealsCacheDatabase(
            database = instance<IDatabaseStudent>().getDataBase()
        )
    }

    bind<IAppealsDbRepository>() with singleton {
        AppealsDbRepositoryImpl(
            instance(),
            instance()
        )
    }

    bind<AppealsTimestampDao>() with singleton {
        AppealsTimestampDatabase(
            database = instance<IDatabaseStudent>().getDataBase()
        )
    }

    bind<IAppealUpdatesRepository>() with singleton {
        AppealsUpdatesRepositoryImpl(
            instance(),
            instance(),
            instance()
        )
    }

}

@ThreadLocal
object AppealsModule{
    val repo: IAppealsApiRepository
        get() = ESSTUSdk.di.instance()

    val db: IAppealsDbRepository
        get() = ESSTUSdk.di.instance()

    val update: IAppealUpdatesRepository
        get() = ESSTUSdk.di.instance()
}

val ESSTUSdk.appealsModule: AppealsModule
    get() = AppealsModule