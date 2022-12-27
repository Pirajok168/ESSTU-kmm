package ru.esstu.student.messaging.messenger.dialogs.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.domain.api.UpdatesApi
import ru.esstu.student.domain.db.DatabaseStudent
import ru.esstu.student.domain.db.IDatabaseStudent
import ru.esstu.student.messaging.messenger.dialogs.datasources.api.DialogsApi
import ru.esstu.student.messaging.messenger.dialogs.datasources.api.DialogsApiImpl
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.cache.CacheDao
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.cache.CacheDatabase
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.*
import ru.esstu.student.messaging.messenger.supports.datasources.api.SupportsApi
import ru.esstu.student.messaging.messenger.supports.datasources.api.SupportsApiImpl
import ru.esstu.student.messaging.messenger.supports.datasources.repo.ISupportsApiRepository
import ru.esstu.student.messaging.messenger.supports.datasources.repo.SupportsApiRepositoryImpl
import kotlin.native.concurrent.ThreadLocal

internal val dialogsModule = DI.Module("dialogsModule"){
    bind<DialogsApi>() with singleton {
        DialogsApiImpl(
            portalApi = instance()
        )
    }

    bind<IDialogsApiRepository>() with singleton {
        DialogsApiRepositoryImpl(
            authRepository = instance(),
            dialogsApi = instance()
        )
    }

    bind<CacheDao>() with singleton {
        CacheDatabase(
            database = instance<IDatabaseStudent>().getDataBase()
        )
    }

    bind<IDialogsDbRepository>() with  singleton {
        DialogsDbRepositoryImpl(
            auth = instance(),
            cacheDao = instance()
        )
    }


    bind<IDialogsUpdatesRepository>() with singleton {
        DialogsUpdatesRepositoryImpl(
            auth = instance(),
            api = instance(),
            timestampDao = instance()
        )
    }

}

@ThreadLocal
object DialogsModule {
    val repo: IDialogsApiRepository
        get() = ESSTUSdk.di.instance()

    val repoDialogs: IDialogsDbRepository
        get() = ESSTUSdk.di.instance()

    val updateDialogs:IDialogsUpdatesRepository
        get() = ESSTUSdk.di.instance()
}

val ESSTUSdk.dialogsModule: DialogsModule
    get() = DialogsModule