package ru.esstu.student.messaging.messenger.dialogs.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.student.messaging.messenger.dialogs.datasources.api.DialogsApi
import ru.esstu.student.messaging.messenger.dialogs.datasources.api.DialogsApiImpl
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.DialogsApiRepositoryImpl
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.IDialogsApiRepository
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

}

@ThreadLocal
object DialogsModule {
    val repo: IDialogsApiRepository
        get() = ESSTUSdk.di.instance()
}

val ESSTUSdk.dialogsModule: DialogsModule
    get() = DialogsModule