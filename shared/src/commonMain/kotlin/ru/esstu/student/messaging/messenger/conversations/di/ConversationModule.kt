package ru.esstu.student.messaging.messenger.conversations.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.student.messaging.messenger.conversations.datasources.api.ConversationApi
import ru.esstu.student.messaging.messenger.conversations.datasources.api.ConversationApiImpl
import ru.esstu.student.messaging.messenger.conversations.datasources.repo.ConversationApiRepositoryImpl
import ru.esstu.student.messaging.messenger.conversations.datasources.repo.IConversationApiRepository
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.IDialogsApiRepository
import ru.esstu.student.messaging.messenger.dialogs.di.DialogsModuleNew
import kotlin.native.concurrent.ThreadLocal

internal val conversationModule = DI.Module("ConversationModule") {

    bind<ConversationApi>() with singleton {
        ConversationApiImpl(
            instance()
        )
    }

    bind<IConversationApiRepository>() with singleton {
        ConversationApiRepositoryImpl(
            instance(),
            instance()
        )
    }

}

@ThreadLocal
object ConversationModule {
    val repo: IConversationApiRepository
        get() = ESSTUSdk.di.instance()
}

val ESSTUSdk.conversationModule: ConversationModule
    get() = ConversationModule