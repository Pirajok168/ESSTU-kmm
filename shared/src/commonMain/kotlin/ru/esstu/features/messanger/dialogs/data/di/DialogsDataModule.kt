package ru.esstu.features.messanger.dialogs.data.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.data.db.IDatabaseStudent
import ru.esstu.features.messanger.dialogs.data.api.DialogsApi
import ru.esstu.features.messanger.dialogs.data.api.DialogsApiImpl
import ru.esstu.features.messanger.dialogs.data.db.dao.DialogsDao
import ru.esstu.features.messanger.dialogs.data.db.dao.DialogsDaoImpl
import ru.esstu.features.messanger.dialogs.data.repository.DialogsApiRepositoryImpl
import ru.esstu.features.messanger.dialogs.data.repository.DialogsUpdatesRepositoryImpl
import ru.esstu.features.messanger.dialogs.data.repository.IDialogsApiRepository
import ru.esstu.features.messanger.dialogs.data.repository.IDialogsUpdatesRepository


fun dialogsDataModule() = DI.Module("dialogsDataModule") {
    bind<DialogsApi>() with singleton {
        DialogsApiImpl(
            authorizedApi = instance()
        )
    }

    bind<IDialogsApiRepository>() with singleton {
        DialogsApiRepositoryImpl(
            dialogsApi = instance()
        )
    }

    bind<DialogsDao>() with singleton {
        DialogsDaoImpl(
            instance<IDatabaseStudent>().getDataBase().dialogQueries
        )
    }

    bind<IDialogsUpdatesRepository>() with singleton {
        DialogsUpdatesRepositoryImpl(
            instance()
        )
    }

}