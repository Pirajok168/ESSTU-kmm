package ru.esstu.student.messaging.messenger.new_message.new_appeal.domain.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.features.messanger.appeal.domain.di.appealDomainModule
import ru.esstu.student.messaging.messenger.new_message.new_appeal.data.di.newAppealDataModule
import ru.esstu.student.messaging.messenger.new_message.new_appeal.domain.repo.INewAppealRepository
import ru.esstu.student.messaging.messenger.new_message.new_appeal.domain.repo.NewAppealRepositoryImpl


fun newAppealDomainModule() = DI.Module("newAppealDomainModule") {
    importOnce(newAppealDataModule())
    importOnce(appealDomainModule())
    bind<INewAppealRepository>() with singleton {
        NewAppealRepositoryImpl(
            instance(),
            instance(),
            instance(),
        )
    }
}