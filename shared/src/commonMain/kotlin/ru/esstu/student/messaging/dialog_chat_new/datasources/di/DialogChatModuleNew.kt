package ru.esstu.student.messaging.dialog_chat_new.datasources.di

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
import ru.esstu.student.messaging.dialog_chat_new.datasources.api.DialogChatApiNew
import ru.esstu.student.messaging.dialog_chat_new.datasources.api.DialogChatApiNewImpl
import ru.esstu.student.messaging.dialog_chat_new.datasources.db.chat_history.HistoryCacheDaoNew
import ru.esstu.student.messaging.dialog_chat_new.datasources.db.chat_history.HistoryCacheDatabaseNew
import ru.esstu.student.messaging.dialog_chat_new.datasources.repo.DialogChatRepositoryNewImpl
import ru.esstu.student.messaging.dialog_chat_new.datasources.repo.IDialogChatRepositoryNew
import kotlin.native.concurrent.ThreadLocal

internal val dialogChatModuleNew = DI.Module("DialogChatModuleNew"){
    bind<DialogChatApiNew>() with singleton {
        DialogChatApiNewImpl(
            portalApi = instance(),
        )
    }
    bind<HistoryCacheDaoNew>() with singleton {
        HistoryCacheDatabaseNew(
            database = instance<IDatabaseStudent>().getDataBase()
        )
    }

    bind<IDialogChatRepositoryNew>() with singleton {
        DialogChatRepositoryNewImpl(
            instance(),
            instance(),
            instance()
        )
    }



}

@ThreadLocal
object DialogChatModule {
    val repo: IDialogChatRepositoryNew
        get() = ESSTUSdk.di.instance()

    val update: IDialogChatUpdateRepository
        get() = ESSTUSdk.di.instance()
}

val ESSTUSdk.dialogChatModuleNew: DialogChatModule
    get() = DialogChatModule