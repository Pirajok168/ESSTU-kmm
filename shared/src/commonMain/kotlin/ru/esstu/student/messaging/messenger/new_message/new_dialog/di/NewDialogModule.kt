package ru.esstu.student.messaging.messenger.new_message.new_dialog.di

import okio.FileSystem
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.domain.modules.account.datasources.datastore._FileSystem
import ru.esstu.domain.modules.account.datasources.datastore.storage
import ru.esstu.student.messaging.messenger.new_message.new_dialog.datasources.api.NewDialogApi
import ru.esstu.student.messaging.messenger.new_message.new_dialog.datasources.api.NewDialogApiImpl
import ru.esstu.student.messaging.messenger.new_message.new_dialog.datasources.repo.INewDialogRepository
import ru.esstu.student.messaging.messenger.new_message.new_dialog.datasources.repo.NewDialogRepositoryImpl
import kotlin.native.concurrent.ThreadLocal

internal val newDialogModule = DI.Module("NewDialogModule") {
    bind<NewDialogApi>() with singleton {
        NewDialogApiImpl(
            instance(),
            fileSystem = storage().fileSystem
        )
    }

    bind<INewDialogRepository>() with singleton {
        NewDialogRepositoryImpl(
            instance(),
            instance(),
            instance()
        )
    }
}

@ThreadLocal
object NewDialogModule {
    val repo: INewDialogRepository
        get() = ESSTUSdk.di.instance()
}

val ESSTUSdk.newDialogModule:  NewDialogModule
    get() = NewDialogModule
