package ru.esstu.student.messaging.group_chat.di

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
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.OpponentDao
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.OpponentDatabase
import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.ErredMessageDao
import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.ErredMessageDatabase
import ru.esstu.student.messaging.dialog_chat.datasources.db.user_messages.UserMessageDatabase
import ru.esstu.student.messaging.dialog_chat.datasources.db.user_messages.UserMessageDao
import ru.esstu.student.messaging.dialog_chat.datasources.repo.DialogChatRepositoryImpl
import ru.esstu.student.messaging.dialog_chat.datasources.repo.DialogChatUpdateRepositoryImpl
import ru.esstu.student.messaging.dialog_chat.datasources.repo.IDialogChatRepository
import ru.esstu.student.messaging.dialog_chat.datasources.repo.IDialogChatUpdateRepository
import ru.esstu.student.messaging.group_chat.datasources.api.GroupChatApi
import ru.esstu.student.messaging.group_chat.datasources.api.GroupChatApiImpl
import ru.esstu.student.messaging.group_chat.datasources.api.GroupChatUpdateApi
import ru.esstu.student.messaging.group_chat.datasources.api.GroupChatUpdateApiImpl
import ru.esstu.student.messaging.group_chat.datasources.db.chat_history.GroupChatHistoryCacheDao
import ru.esstu.student.messaging.group_chat.datasources.db.chat_history.GroupChatHistoryCacheDatabase
import ru.esstu.student.messaging.group_chat.datasources.db.header.HeaderDao
import ru.esstu.student.messaging.group_chat.datasources.db.header.HeaderDatabase
import ru.esstu.student.messaging.group_chat.datasources.db.user_messages.GroupChatUserMessageDatabase
import ru.esstu.student.messaging.group_chat.datasources.db.user_messages.GroupUserMessageDao
import ru.esstu.student.messaging.group_chat.datasources.repo.GroupChatRepositoryImpl
import ru.esstu.student.messaging.group_chat.datasources.repo.IGroupChatRepository
import kotlin.native.concurrent.ThreadLocal

internal val groupChatModuleNew = DI.Module("groupChatModuleNew"){
    bind<GroupChatApi>() with singleton {
        GroupChatApiImpl(
            portalApi = instance(),
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

    bind<IGroupChatRepository>() with singleton {
        GroupChatRepositoryImpl(
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )
    }


}

@ThreadLocal
object GroupChatModuleNew {
    val repo: IGroupChatRepository
        get() = ESSTUSdk.di.instance()

    val update: IDialogChatUpdateRepository
        get() = ESSTUSdk.di.instance()
}

val ESSTUSdk.groupChatModuleNew: GroupChatModuleNew
    get() = GroupChatModuleNew