package ru.esstu.student.messaging.messenger.new_message.new_appeal.di

import okio.FileSystem
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.domain.modules.account.datasources.datastore._FileSystem
import ru.esstu.domain.modules.account.datasources.datastore.storage
import ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api.NewAppealApi
import ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api.NewAppealApiImpl
import ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.repo.INewAppealRepository
import ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.repo.NewAppealRepositoryImpl
import ru.esstu.student.messaging.messenger.new_message.new_dialog.datasources.api.NewDialogApi
import ru.esstu.student.messaging.messenger.new_message.new_dialog.datasources.api.NewDialogApiImpl
import ru.esstu.student.messaging.messenger.new_message.new_dialog.datasources.repo.INewDialogRepository
import ru.esstu.student.messaging.messenger.new_message.new_dialog.datasources.repo.NewDialogRepositoryImpl
import ru.esstu.student.messaging.messenger.new_message.new_support.datasources.api.NewSupportApi
import ru.esstu.student.messaging.messenger.new_message.new_support.datasources.api.NewSupportApiImpl
import ru.esstu.student.messaging.messenger.new_message.new_support.datasources.repo.INewSupportRepository
import ru.esstu.student.messaging.messenger.new_message.new_support.datasources.repo.NewSupportRepositoryImpl
import kotlin.native.concurrent.ThreadLocal

internal val newAppealModule = DI.Module("NewAppealModule") {
    bind<NewAppealApi>() with singleton {
        NewAppealApiImpl(
            instance(),
            fileSystem = storage().fileSystem
        )
    }

    bind<INewAppealRepository>() with singleton {
        NewAppealRepositoryImpl(
            instance(),
            instance(),
            instance(),
        )
    }
}

@ThreadLocal
object NewAppealModule {
    val repo: INewAppealRepository
        get() = ESSTUSdk.di.instance()
}

val ESSTUSdk.newAppealModule:  NewAppealModule
    get() = NewAppealModule
