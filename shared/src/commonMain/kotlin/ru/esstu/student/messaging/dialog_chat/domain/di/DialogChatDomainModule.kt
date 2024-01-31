package ru.esstu.student.messaging.dialog_chat.domain.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.features.messanger.dialogs.data.di.dialogsDataModule
import ru.esstu.student.messaging.dialog_chat.data.di.dialogChatDataModule
import ru.esstu.student.messaging.dialog_chat.domain.repo.DialogChatRepositoryImpl
import ru.esstu.student.messaging.dialog_chat.domain.repo.DialogChatUpdateRepositoryImpl
import ru.esstu.student.messaging.dialog_chat.domain.repo.IDialogChatRepository
import ru.esstu.student.messaging.dialog_chat.domain.repo.IDialogChatUpdateRepository


fun dialogChatDomainModule() = DI.Module("DialogChatDomainModule") {
    importOnce(dialogChatDataModule())
    importOnce(dialogsDataModule())
    bind<IDialogChatRepository>() with singleton {
        DialogChatRepositoryImpl(
            dialogChatApi = instance(),
            cacheDao = instance(),
            opponentDao = instance(),
            userMsgDao = instance(),
            erredMsgDao = instance(),
            loginDataRepository = instance()
        )
    }



    bind<IDialogChatUpdateRepository>() with singleton {
        DialogChatUpdateRepositoryImpl(
            updateApi = instance(),
            chatApi = instance()
        )
    }
}