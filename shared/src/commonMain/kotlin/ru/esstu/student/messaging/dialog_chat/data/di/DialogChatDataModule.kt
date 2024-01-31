package ru.esstu.student.messaging.dialog_chat.data.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.data.db.IDatabaseStudent
import ru.esstu.data.fileSystem.storage
import ru.esstu.student.messaging.dialog_chat.data.api.DialogChatApi
import ru.esstu.student.messaging.dialog_chat.data.api.DialogChatApiImpl
import ru.esstu.student.messaging.dialog_chat.data.api.DialogChatUpdateApi
import ru.esstu.student.messaging.dialog_chat.data.api.DialogChatUpdateApiImpl
import ru.esstu.student.messaging.dialog_chat.data.db.chat_history.HistoryCacheDao
import ru.esstu.student.messaging.dialog_chat.data.db.chat_history.HistoryCacheDatabase
import ru.esstu.student.messaging.dialog_chat.data.db.chat_history.OpponentDao
import ru.esstu.student.messaging.dialog_chat.data.db.chat_history.OpponentDatabase
import ru.esstu.student.messaging.dialog_chat.data.db.erred_messages.ErredMessageDao
import ru.esstu.student.messaging.dialog_chat.data.db.erred_messages.ErredMessageDatabase
import ru.esstu.student.messaging.dialog_chat.data.db.user_messages.UserMessageDao
import ru.esstu.student.messaging.dialog_chat.data.db.user_messages.UserMessageDatabase


fun dialogChatDataModule() = DI.Module("dialogChatDataModule") {
    bind<DialogChatApi>() with singleton {
        DialogChatApiImpl(
            authorizedApi = instance(),
            fileSystem = storage().fileSystem
        )
    }
    bind<HistoryCacheDao>() with singleton {
        HistoryCacheDatabase(
            database = instance<IDatabaseStudent>().getDataBase()
        )
    }
    bind<OpponentDao>() with singleton {
        OpponentDatabase(
            database = instance<IDatabaseStudent>().getDataBase()
        )
    }
    bind<UserMessageDao>() with singleton {
        UserMessageDatabase(
            instance<IDatabaseStudent>().getDataBase()
        )
    }
    bind<ErredMessageDao>() with singleton {
        ErredMessageDatabase(
            instance<IDatabaseStudent>().getDataBase()
        )
    }

    bind<DialogChatUpdateApi>() with singleton {
        DialogChatUpdateApiImpl(
            instance()
        )
    }
}