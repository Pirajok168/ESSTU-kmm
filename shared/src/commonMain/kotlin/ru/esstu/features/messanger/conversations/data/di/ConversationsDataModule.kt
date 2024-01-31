package ru.esstu.features.messanger.conversations.data.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.data.db.IDatabaseStudent
import ru.esstu.features.messanger.conversations.data.api.ConversationsApi
import ru.esstu.features.messanger.conversations.data.api.ConversationsApiImpl
import ru.esstu.features.messanger.conversations.data.db.ConversationsCacheDao
import ru.esstu.features.messanger.conversations.data.db.ConversationsCacheDatabase
import ru.esstu.features.messanger.conversations.data.repository.ConversationUpdatesRepositoryImpl
import ru.esstu.features.messanger.conversations.data.repository.ConversationsApiRepositoryImpl
import ru.esstu.features.messanger.conversations.data.repository.IConversationUpdatesRepository
import ru.esstu.features.messanger.conversations.data.repository.IConversationsApiRepository


fun conversationsDataModule() = DI.Module("ConversationsDataModule") {
    bind<ConversationsApi>() with singleton {
        ConversationsApiImpl(
            instance()
        )
    }

    bind<ConversationsCacheDao>() with singleton {
        ConversationsCacheDatabase(
            instance<IDatabaseStudent>().getDataBase().conversationQueries
        )
    }

    bind<IConversationsApiRepository>() with singleton {
        ConversationsApiRepositoryImpl(
            instance(),
        )
    }

    bind<IConversationUpdatesRepository>() with singleton {
        ConversationUpdatesRepositoryImpl(
            instance()
        )
    }
}