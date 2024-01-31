package ru.esstu.student.messaging.messenger.new_message.new_dialog.di

import org.kodein.di.DI
import ru.esstu.di.sharedDi
import ru.esstu.features.messanger.dialogs.domain.di.dialogsDomainModule
import ru.esstu.student.messaging.messenger.new_message.new_dialog.domain.di.newDialogDomainModule


fun newDialogDi() = DI {
    extend(sharedDi)
    importOnce(dialogsDomainModule())
    importOnce(newDialogDomainModule())
}