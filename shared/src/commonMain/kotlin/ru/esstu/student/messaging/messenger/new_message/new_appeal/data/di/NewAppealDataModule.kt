package ru.esstu.student.messaging.messenger.new_message.new_appeal.data.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.data.fileSystem.storage
import ru.esstu.student.messaging.messenger.new_message.new_appeal.data.api.NewAppealApi
import ru.esstu.student.messaging.messenger.new_message.new_appeal.data.api.NewAppealApiImpl


fun newAppealDataModule() = DI.Module("newAppealDataModule") {
    bind<NewAppealApi>() with singleton {
        NewAppealApiImpl(
            instance(),
            fileSystem = storage().fileSystem
        )
    }

}