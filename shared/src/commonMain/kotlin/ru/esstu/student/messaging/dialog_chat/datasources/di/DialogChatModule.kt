package ru.esstu.student.messaging.dialog_chat.datasources.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.domain.modules.account.datasources.datastore.storage
import ru.esstu.student.domain.db.IDatabaseStudent
import ru.esstu.student.messaging.dialog_chat.datasources.api.DialogChatApi
import ru.esstu.student.messaging.dialog_chat.datasources.api.DialogChatApiImpl
import ru.esstu.student.messaging.dialog_chat.datasources.api.DialogChatUpdateApi
import ru.esstu.student.messaging.dialog_chat.datasources.api.DialogChatUpdateApiImpl
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.HistoryCacheDao
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.HistoryCacheDatabase
import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.ErredMessageDao
import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.ErredMessageDatabase
import ru.esstu.student.messaging.dialog_chat.datasources.db.user_message.UserMessageDao
import ru.esstu.student.messaging.dialog_chat.datasources.db.user_message.UserMessageDatabase
import ru.esstu.student.messaging.dialog_chat.datasources.repo.DialogChatRepositoryImpl
import ru.esstu.student.messaging.dialog_chat.datasources.repo.DialogChatUpdateRepositoryImpl
import ru.esstu.student.messaging.dialog_chat.datasources.repo.IDialogChatRepository
import ru.esstu.student.messaging.dialog_chat.datasources.repo.IDialogChatUpdateRepository
import kotlin.native.concurrent.ThreadLocal

internal val dialogChatModule = DI.Module("DialogChatModule"){
    bind<DialogChatApi>() with singleton {
        DialogChatApiImpl(
            portalApi = instance(),
            storage().fileSystem
        )
    }

    bind<DialogChatUpdateApi>() with singleton {
        DialogChatUpdateApiImpl(
            portalApi = instance()
        )
    }

    bind<ErredMessageDao>() with singleton {
        ErredMessageDatabase(
            instance<IDatabaseStudent>().getDataBase()
        )
    }

    bind<HistoryCacheDao>() with singleton {
        HistoryCacheDatabase(
            instance<IDatabaseStudent>().getDataBase()
        )
    }

    bind<UserMessageDao>() with singleton {
        UserMessageDatabase(
            instance<IDatabaseStudent>().getDataBase()
        )
    }

    bind<IDialogChatRepository>() with singleton {
        DialogChatRepositoryImpl(
            auth = instance(),
            dialogChatApi = instance(),
            cacheDao = instance(),
            userMsgDao = instance(),
            erredMsgDao = instance(),
            instance()
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