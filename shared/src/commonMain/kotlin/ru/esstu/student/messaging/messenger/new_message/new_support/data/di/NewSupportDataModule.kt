package ru.esstu.student.messaging.messenger.new_message.new_support.data.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.data.fileSystem.storage
import ru.esstu.student.messaging.messenger.new_message.new_support.data.api.NewSupportApi
import ru.esstu.student.messaging.messenger.new_message.new_support.data.api.NewSupportApiImpl


fun newSupportDataModule() = DI.Module("newSupportDataModule") {
    bind<NewSupportApi>() with singleton {
        NewSupportApiImpl(
            instance(),
            fileSystem = storage().fileSystem
        )
    }
}