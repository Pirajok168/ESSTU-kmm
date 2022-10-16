package ru.esstu.student.messaging.dialog_chat.datasources.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.student.messaging.dialog_chat.datasources.api.DialogChatApi
import ru.esstu.student.messaging.dialog_chat.datasources.api.DialogChatApiImpl
import ru.esstu.student.messaging.dialog_chat.datasources.api.DialogChatUpdateApi
import ru.esstu.student.messaging.dialog_chat.datasources.api.DialogChatUpdateApiImpl
import ru.esstu.student.messaging.dialog_chat.datasources.repo.DialogChatRepositoryImpl
import ru.esstu.student.messaging.dialog_chat.datasources.repo.DialogChatUpdateRepositoryImpl
import ru.esstu.student.messaging.dialog_chat.datasources.repo.IDialogChatRepository
import ru.esstu.student.messaging.dialog_chat.datasources.repo.IDialogChatUpdateRepository
import kotlin.native.concurrent.ThreadLocal

internal val dialogChatModule = DI.Module("DialogChatModule"){
    bind<DialogChatApi>() with singleton {
        DialogChatApiImpl(
            portalApi = instance()
        )
    }

    bind<DialogChatUpdateApi>() with singleton {
        DialogChatUpdateApiImpl(
            portalApi = instance()
        )
    }

    bind<IDialogChatRepository>() with singleton {
        DialogChatRepositoryImpl(
            auth = instance(),
            dialogChatApi = instance()
        )
    }

    bind<IDialogChatUpdateRepository>() with  singleton {
        DialogChatUpdateRepositoryImpl(
            auth = instance(),
            updateApi = instance(),
            chatApi = instance()
        )
    }

}

@ThreadLocal
object DialogChatModule {
    val repo: IDialogChatRepository
        get() = ESSTUSdk.di.instance()

    val update: IDialogChatUpdateRepository
        get() = ESSTUSdk.di.instance()
}

val ESSTUSdk.dialogChatModule: DialogChatModule
    get() = DialogChatModule