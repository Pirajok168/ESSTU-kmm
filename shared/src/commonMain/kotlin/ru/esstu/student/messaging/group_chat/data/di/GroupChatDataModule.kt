package ru.esstu.student.messaging.group_chat.data.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.data.db.IDatabaseStudent
import ru.esstu.data.fileSystem.storage
import ru.esstu.student.messaging.group_chat.data.api.GroupChatApi
import ru.esstu.student.messaging.group_chat.data.api.GroupChatApiImpl
import ru.esstu.student.messaging.group_chat.data.api.GroupChatUpdateApi
import ru.esstu.student.messaging.group_chat.data.api.GroupChatUpdateApiImpl
import ru.esstu.student.messaging.group_chat.data.db.chat_history.GroupChatHistoryCacheDao
import ru.esstu.student.messaging.group_chat.data.db.chat_history.GroupChatHistoryCacheDatabase
import ru.esstu.student.messaging.group_chat.data.db.erred_messages.ErredMessageDao
import ru.esstu.student.messaging.group_chat.data.db.erred_messages.ErredMessageDatabase
import ru.esstu.student.messaging.group_chat.data.db.header.HeaderDao
import ru.esstu.student.messaging.group_chat.data.db.header.HeaderDatabase
import ru.esstu.student.messaging.group_chat.data.db.user_messages.GroupChatUserMessageDatabase
import ru.esstu.student.messaging.group_chat.data.db.user_messages.GroupUserMessageDao


fun groupChatDataModule() = DI.Module("GroupChatDataModule") {
    bind<GroupChatApi>() with singleton {
        GroupChatApiImpl(
            authorizedApi = instance(),
            storage().fileSystem
        )
    }
    bind<GroupChatHistoryCacheDao>() with singleton {
        GroupChatHistoryCacheDatabase(
            database = instance<IDatabaseStudent>().getDataBase()
        )
    }



    bind<GroupChatUpdateApi>() with singleton {
        GroupChatUpdateApiImpl(
            instance()
        )
    }

    bind<ErredMessageDao>() with singleton {
        ErredMessageDatabase(
            instance<IDatabaseStudent>().getDataBase()
        )
    }

    bind<GroupUserMessageDao>() with singleton {
        GroupChatUserMessageDatabase(
            instance<IDatabaseStudent>().getDataBase()
        )
    }

    bind<HeaderDao>() with singleton {
        HeaderDatabase(
            instance<IDatabaseStudent>().getDataBase()
        )
    }
}