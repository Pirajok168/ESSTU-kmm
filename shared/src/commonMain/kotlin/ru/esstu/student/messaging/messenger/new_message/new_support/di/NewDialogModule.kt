package ru.esstu.student.messaging.messenger.new_message.new_support.di

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
import ru.esstu.student.messaging.messenger.new_message.new_support.datasources.api.NewSupportApi
import ru.esstu.student.messaging.messenger.new_message.new_support.datasources.api.NewSupportApiImpl
import ru.esstu.student.messaging.messenger.new_message.new_support.datasources.repo.INewSupportRepository
import ru.esstu.student.messaging.messenger.new_message.new_support.datasources.repo.NewSupportRepositoryImpl
import kotlin.native.concurrent.ThreadLocal

internal val newSupportModule = DI.Module("NewSupportModule") {
    bind<NewSupportApi>() with singleton {
        NewSupportApiImpl(
            instance(),
            fileSystem = storage().fileSystem
        )
    }

    bind<INewSupportRepository>() with singleton {
        NewSupportRepositoryImpl(
            instance(),
            instance(),
            instance(),
        )
    }
}

@ThreadLocal
object NewSupportModule {
    val repo: INewSupportRepository
        get() = ESSTUSdk.di.instance()
}

val ESSTUSdk.newSupportModule:  NewSupportModule
    get() = NewSupportModule
