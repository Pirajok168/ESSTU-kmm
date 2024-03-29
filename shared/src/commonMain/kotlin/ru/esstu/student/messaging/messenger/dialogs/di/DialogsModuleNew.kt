package ru.esstu.student.messaging.messenger.dialogs.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.student.domain.db.IDatabaseStudent
import ru.esstu.student.messaging.messenger.dialogs.datasources.api.DialogsApi
import ru.esstu.student.messaging.messenger.dialogs.datasources.api.DialogsApiImpl
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.CacheDao
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.CacheDatabase
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.DialogsTimestampDao
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.DialogsTimestampDatabase
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.DialogsApiRepositoryImpl
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.DialogsDbRepositoryImpl
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.DialogsRepositoryImpl
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.DialogsUpdatesRepositoryImpl
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.IDialogsApiRepository
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.IDialogsDbRepository
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.IDialogsRepository
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.IDialogsUpdatesRepository
import kotlin.native.concurrent.ThreadLocal

internal val dialogsModuleNew = DI.Module("dialogsModuleNew") {
    bind<DialogsApi>() with singleton {
        DialogsApiImpl(
            authorizedApi = instance()
        )
    }
    bind<IDialogsApiRepository>() with singleton {
        DialogsApiRepositoryImpl(
            dialogsApi = instance()
        )
    }





    bind<CacheDao>() with singleton {
        CacheDatabase(
            database = instance<IDatabaseStudent>().getDataBase(),
        )
    }

    bind<IDialogsDbRepository>() with singleton {
        DialogsDbRepositoryImpl(
            auth = instance(),
            cacheDao = instance()
        )
    }
    bind<DialogsTimestampDao>() with singleton {
        DialogsTimestampDatabase(
            database = instance<IDatabaseStudent>().getDataBase(),
        )
    }

    bind<IDialogsUpdatesRepository>() with singleton {
        DialogsUpdatesRepositoryImpl(
            instance(),
            instance(),
            instance()
        )
    }

    bind<IDialogsRepository>() with singleton { DialogsRepositoryImpl(instance(), instance()) }
}

@ThreadLocal
object DialogsModuleNew {
    val repo: IDialogsApiRepository
        get() = ESSTUSdk.di.instance()

    val repoDialogs: IDialogsDbRepository
        get() = ESSTUSdk.di.instance()

    val update: IDialogsUpdatesRepository
        get() = ESSTUSdk.di.instance()

    val abstractRepo: IDialogsRepository
        get() = ESSTUSdk.di.instance()
}

val ESSTUSdk.dialogsModuleNew: DialogsModuleNew
    get() = DialogsModuleNew