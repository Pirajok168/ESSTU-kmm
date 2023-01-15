package ru.esstu.student.messaging.messenger.supports.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.student.domain.db.IDatabaseStudent
import ru.esstu.student.messaging.messenger.supports.datasource.api.SupportsApi
import ru.esstu.student.messaging.messenger.supports.datasource.api.SupportsApiImpl
import ru.esstu.student.messaging.messenger.supports.datasource.db.SupportsCacheDao
import ru.esstu.student.messaging.messenger.supports.datasource.db.SupportsCacheDatabase
import ru.esstu.student.messaging.messenger.supports.datasource.repo.ISupportsApiRepository
import ru.esstu.student.messaging.messenger.supports.datasource.repo.ISupportsDbRepository
import ru.esstu.student.messaging.messenger.supports.datasource.repo.SupportsApiRepositoryImpl
import ru.esstu.student.messaging.messenger.supports.datasource.repo.SupportsDbRepositoryImpl
import kotlin.native.concurrent.ThreadLocal

internal val supportsModule = DI.Module("SupportsModule") {
    bind<SupportsApi>() with singleton {
        SupportsApiImpl(
            instance()
        )
    }

    bind<ISupportsApiRepository>() with singleton {
        SupportsApiRepositoryImpl(
            instance(),
            instance()
        )
    }

    bind<SupportsCacheDao>() with singleton {
        SupportsCacheDatabase(
            database = instance<IDatabaseStudent>().getDataBase()
        )
    }

    bind<ISupportsDbRepository>() with singleton {
        SupportsDbRepositoryImpl(
            instance(),
            instance(),
        )
    }

}


@ThreadLocal
object SupportsModule {
    val apiRepo: ISupportsApiRepository
        get() = ESSTUSdk.di.instance()

    val dbRepo: ISupportsDbRepository
        get() = ESSTUSdk.di.instance()
}

val ESSTUSdk.supportsModule:SupportsModule
    get() = SupportsModule
