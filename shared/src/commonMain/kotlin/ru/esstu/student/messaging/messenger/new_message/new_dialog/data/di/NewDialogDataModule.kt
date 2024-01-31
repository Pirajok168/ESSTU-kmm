package ru.esstu.student.messaging.messenger.new_message.new_dialog.data.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.data.fileSystem.storage
import ru.esstu.student.messaging.messenger.new_message.new_dialog.data.api.NewDialogApi
import ru.esstu.student.messaging.messenger.new_message.new_dialog.data.api.NewDialogApiImpl


fun newDialogDataModule() = DI.Module("newDialogDataModule") {
    bind<NewDialogApi>() with singleton {
        NewDialogApiImpl(
            instance(),
            fileSystem = storage().fileSystem
        )
    }
}