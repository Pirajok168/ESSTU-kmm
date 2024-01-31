package ru.esstu.student.messaging.group_chat.domain.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.features.messanger.appeal.data.di.appealsDataModule
import ru.esstu.features.messanger.supports.data.di.supportsDataModule
import ru.esstu.student.messaging.group_chat.data.di.groupChatDataModule
import ru.esstu.student.messaging.group_chat.domain.repo.GroupChatRepositoryImpl
import ru.esstu.student.messaging.group_chat.domain.repo.GroupChatUpdateRepositoryImpl
import ru.esstu.student.messaging.group_chat.domain.repo.IGroupChatRepository
import ru.esstu.student.messaging.group_chat.domain.repo.IGroupChatUpdateRepository


fun groupChatDomainModule() = DI.Module("groupChatDomainModule") {
    importOnce(groupChatDataModule())
    importOnce(supportsDataModule())
    importOnce(appealsDataModule())
    bind<IGroupChatRepository>() with singleton {
        GroupChatRepositoryImpl(
            groupChatApi = instance(),
            historyCacheDao = instance(),
            cache = instance(),
            headerDao = instance(),
            userMsgDao = instance(),
            erredMsgDao = instance(),
            supportMsgDao = instance(),
            appealMsgDao = instance(),
            loginDataRepository = instance()
        )
    }

    bind<IGroupChatUpdateRepository>() with singleton {
        GroupChatUpdateRepositoryImpl(
            instance(),
            instance(),
        )
    }
}