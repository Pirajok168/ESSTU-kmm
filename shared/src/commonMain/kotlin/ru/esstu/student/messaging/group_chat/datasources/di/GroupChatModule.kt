package ru.esstu.student.messaging.group_chat.datasources.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.student.messaging.group_chat.datasources.api.GroupChatApi
import ru.esstu.student.messaging.group_chat.datasources.api.GroupChatApiImpl
import ru.esstu.student.messaging.group_chat.datasources.api.GroupChatUpdateApi
import ru.esstu.student.messaging.group_chat.datasources.api.GroupChatUpdateApiImpl
import ru.esstu.student.messaging.group_chat.datasources.repo.GroupChatRepositoryImpl
import ru.esstu.student.messaging.group_chat.datasources.repo.GroupChatUpdateRepositoryImpl
import ru.esstu.student.messaging.group_chat.datasources.repo.IGroupChatRepository
import ru.esstu.student.messaging.group_chat.datasources.repo.IGroupChatUpdateRepository
import kotlin.native.concurrent.ThreadLocal

internal val groupChatModule = DI.Module("groupChatModule") {
    bind<GroupChatApi>() with  singleton {
        GroupChatApiImpl(
            portalApi = instance()
        )
    }

    bind<GroupChatUpdateApi>() with  singleton {
        GroupChatUpdateApiImpl(
            portalApi = instance()
        )
    }

    bind<IGroupChatRepository>() with singleton{
        GroupChatRepositoryImpl(
            auth = instance(),
            groupChatApi = instance()
        )
    }

    bind<IGroupChatUpdateRepository>() with singleton {
        GroupChatUpdateRepositoryImpl(
            auth = instance(),
            updateApi = instance(),
            chatApi = instance()
        )
    }

}

@ThreadLocal
object GroupChatModule {
    val repo: IGroupChatRepository
        get() = ESSTUSdk.di.instance()

    val updates: IGroupChatUpdateRepository
        get() = ESSTUSdk.di.instance()
}

val ESSTUSdk.groupChatModule: GroupChatModule
    get() = GroupChatModule