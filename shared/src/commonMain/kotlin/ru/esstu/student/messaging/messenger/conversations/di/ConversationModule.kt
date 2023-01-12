package ru.esstu.student.messaging.messenger.conversations.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.student.domain.db.IDatabaseStudent
import ru.esstu.student.messaging.messenger.conversations.datasources.api.ConversationsApi
import ru.esstu.student.messaging.messenger.conversations.datasources.api.ConversationsApiImpl
import ru.esstu.student.messaging.messenger.conversations.datasources.db.ConversationsCacheDao
import ru.esstu.student.messaging.messenger.conversations.datasources.db.ConversationsCacheDatabase
import ru.esstu.student.messaging.messenger.conversations.datasources.repo.*
import kotlin.native.concurrent.ThreadLocal

internal val conversationModule = DI.Module("ConversationModule") {

    bind<ConversationsApi>() with singleton {
        ConversationsApiImpl(
            instance()
        )
    }

    bind<IConversationsApiRepository>() with singleton {
        ConversationsApiRepositoryImpl(
            instance(),
            instance()
        )
    }

    bind<ConversationsCacheDao>() with singleton {
        ConversationsCacheDatabase(
            database = instance<IDatabaseStudent>().getDataBase()
        )
    }

    bind<IConversationsDbRepository>() with singleton {
        ConversationsDbRepositoryImpl(
            instance(),
            instance()
        )
    }

    bind<IConversationUpdatesRepository>() with singleton {
        ConversationUpdatesRepositoryImpl(
            instance(),
            instance(),
            instance()
        )
    }
}

@ThreadLocal
object ConversationModule {
    val repo: IConversationsApiRepository
        get() = ESSTUSdk.di.instance()

    val db: IConversationsDbRepository
        get() = ESSTUSdk.di.instance()

    val update: IConversationUpdatesRepository
        get() = ESSTUSdk.di.instance()
}

val ESSTUSdk.conversationModule: ConversationModule
    get() = ConversationModule