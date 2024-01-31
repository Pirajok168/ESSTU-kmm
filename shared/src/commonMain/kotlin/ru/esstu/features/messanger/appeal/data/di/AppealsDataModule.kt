package ru.esstu.features.messanger.appeal.data.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.data.db.IDatabaseStudent
import ru.esstu.features.messanger.appeal.data.api.AppealsApi
import ru.esstu.features.messanger.appeal.data.api.AppealsApiImpl
import ru.esstu.features.messanger.appeal.data.db.AppealCacheDatabase
import ru.esstu.features.messanger.appeal.data.db.AppealsCacheDao
import ru.esstu.features.messanger.appeal.data.repository.AppealsApiRepositoryImpl
import ru.esstu.features.messanger.appeal.data.repository.AppealsUpdatesRepositoryImpl
import ru.esstu.features.messanger.appeal.data.repository.IAppealUpdatesRepository
import ru.esstu.features.messanger.appeal.data.repository.IAppealsApiRepository


fun appealsDataModule() = DI.Module("AppealsDataModule") {
    bind<AppealsApi>() with singleton {
        AppealsApiImpl(
            instance()
        )
    }


    bind<AppealsCacheDao>() with singleton {
        AppealCacheDatabase(
            instance<IDatabaseStudent>().getDataBase().appealsQueries
        )
    }

    bind<IAppealsApiRepository>() with singleton {
        AppealsApiRepositoryImpl(
            instance()
        )
    }

    bind<IAppealUpdatesRepository>() with singleton {
        AppealsUpdatesRepositoryImpl(
            instance()
        )
    }
}