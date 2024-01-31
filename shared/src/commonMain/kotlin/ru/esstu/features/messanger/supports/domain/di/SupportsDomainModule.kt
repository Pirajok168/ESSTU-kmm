package ru.esstu.features.messanger.supports.domain.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.features.messanger.supports.data.di.supportsDataModule
import ru.esstu.features.messanger.supports.domain.interactor.SupportInteractor
import ru.esstu.features.messanger.supports.domain.interactor.SupportInteractorImpl


fun supportsDomainModule() = DI.Module("SupportsDomainModule") {
    importOnce(supportsDataModule())



    bind<SupportInteractor> {
        singleton {
            SupportInteractorImpl(
                instance(),
                instance()
            )
        }
    }
}