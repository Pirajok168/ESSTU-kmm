package ru.esstu.features.messanger.dialogs.domain.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.features.messanger.dialogs.data.di.dialogsDataModule
import ru.esstu.features.messanger.dialogs.domain.interactor.DialogsInteractor
import ru.esstu.features.messanger.dialogs.domain.interactor.DialogsInteractorImpl


fun dialogsDomainModule() = DI.Module("dialogsDomainModule") {
    importOnce(dialogsDataModule())

    bind<DialogsInteractor>() with singleton {
        DialogsInteractorImpl(
            instance(),
            instance()
        )
    }

}