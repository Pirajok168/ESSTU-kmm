package ru.esstu.features.messanger.conversations.domain.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.features.messanger.conversations.data.di.conversationsDataModule
import ru.esstu.features.messanger.conversations.domain.interactor.ConversationsInteractor
import ru.esstu.features.messanger.conversations.domain.interactor.ConversationsInteractorImpl


fun conversationsDomainModule() = DI.Module("conversationsDomainModule") {
    importOnce(conversationsDataModule())




    bind<ConversationsInteractor>() with singleton {
        ConversationsInteractorImpl(
            instance(),
            instance()
        )
    }


}