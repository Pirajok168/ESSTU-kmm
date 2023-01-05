package ru.esstu.student.messaging.messenger.dialogs.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
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
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.*
import kotlin.native.concurrent.ThreadLocal

internal val dialogsModuleNew = DI.Module("dialogsModuleNew"){
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
            database = instance<IDatabaseStudent>().getDataBase(),
        )
    }

    bind<IDialogsDbRepository>() with  singleton {
        DialogsDbRepositoryImpl(
            auth = instance(),
            cacheDao = instance()
        )
    }
}

@ThreadLocal
object DialogsModuleNew {
    val repo: IDialogsApiRepository
        get() = ESSTUSdk.di.instance()

    val repoDialogs: IDialogsDbRepository
        get() = ESSTUSdk.di.instance()

}

val ESSTUSdk.dialogsModuleNew: DialogsModuleNew
    get() = DialogsModuleNew