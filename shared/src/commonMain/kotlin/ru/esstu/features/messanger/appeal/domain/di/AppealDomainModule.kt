package ru.esstu.features.messanger.appeal.domain.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.features.messanger.appeal.data.di.appealsDataModule
import ru.esstu.features.messanger.appeal.domain.interactor.AppealsInteractor
import ru.esstu.features.messanger.appeal.domain.interactor.AppealsInteractorImpl


fun appealDomainModule() = DI.Module("AppealDomainModule") {
    importOnce(appealsDataModule())



    bind<AppealsInteractor> {
        singleton {
            AppealsInteractorImpl(
                instance(),
                instance()
            )
        }
    }
}