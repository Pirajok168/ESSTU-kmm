package ru.esstu.student.messaging.messenger.new_message.new_support.domain.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.features.messanger.supports.data.di.supportsDataModule
import ru.esstu.student.messaging.messenger.new_message.new_support.data.di.newSupportDataModule
import ru.esstu.student.messaging.messenger.new_message.new_support.domain.repo.INewSupportRepository
import ru.esstu.student.messaging.messenger.new_message.new_support.domain.repo.NewSupportRepositoryImpl


fun newSupportDomainModule() = DI.Module("NewSupportDomainModule") {
    importOnce(newSupportDataModule())
    importOnce(supportsDataModule())
    bind<INewSupportRepository>() with singleton {
        NewSupportRepositoryImpl(
            instance(),
            instance(),
            instance(),
        )
    }
}

// TODO Не создаётся обращение в деканаты + не создаётся техподдержка по сайту(странно)