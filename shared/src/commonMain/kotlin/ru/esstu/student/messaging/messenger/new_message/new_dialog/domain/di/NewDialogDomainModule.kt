package ru.esstu.student.messaging.messenger.new_message.new_dialog.domain.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.student.messaging.messenger.new_message.new_dialog.data.di.newDialogDataModule
import ru.esstu.student.messaging.messenger.new_message.new_dialog.domain.repo.INewDialogRepository
import ru.esstu.student.messaging.messenger.new_message.new_dialog.domain.repo.NewDialogRepositoryImpl


fun newDialogDomainModule() = DI.Module("NewDialogDomainModule") {
    importOnce(newDialogDataModule())

    bind<INewDialogRepository>() with singleton {
        NewDialogRepositoryImpl(
            instance(),
            instance()
        )
    }
}