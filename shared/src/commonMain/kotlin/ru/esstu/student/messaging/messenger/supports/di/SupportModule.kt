package ru.esstu.student.messaging.messenger.supports.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.student.messaging.messenger.conversations.datasources.api.ConversationApi
import ru.esstu.student.messaging.messenger.conversations.datasources.api.ConversationApiImpl
import ru.esstu.student.messaging.messenger.conversations.datasources.repo.ConversationApiRepositoryImpl
import ru.esstu.student.messaging.messenger.conversations.datasources.repo.IConversationApiRepository
import ru.esstu.student.messaging.messenger.supports.datasources.api.SupportsApi
import ru.esstu.student.messaging.messenger.supports.datasources.api.SupportsApiImpl
import ru.esstu.student.messaging.messenger.supports.datasources.repo.ISupportsApiRepository
import ru.esstu.student.messaging.messenger.supports.datasources.repo.SupportsApiRepositoryImpl
import kotlin.native.concurrent.ThreadLocal

internal val supportModule = DI.Module("supportModule"){
    bind<SupportsApi>() with singleton {
        SupportsApiImpl(
            portalApi = instance()
        )
    }

    bind<ISupportsApiRepository>() with singleton {
        SupportsApiRepositoryImpl(
            auth = instance(),
            api = instance()
        )
    }

}

@ThreadLocal
object SupportModule {
    val repo: ISupportsApiRepository
        get() = ESSTUSdk.di.instance()
}

val ESSTUSdk.appealsModule: SupportModule
    get() = SupportModule