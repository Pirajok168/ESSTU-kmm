package ru.esstu.features.messanger.supports.data.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.data.db.IDatabaseStudent
import ru.esstu.features.messanger.supports.data.api.SupportsApi
import ru.esstu.features.messanger.supports.data.api.SupportsApiImpl
import ru.esstu.features.messanger.supports.data.db.SupportsCacheDao
import ru.esstu.features.messanger.supports.data.db.SupportsCacheDatabase
import ru.esstu.features.messanger.supports.data.repository.ISupportsApiRepository
import ru.esstu.features.messanger.supports.data.repository.ISupportsUpdatesRepository
import ru.esstu.features.messanger.supports.data.repository.SupportsApiRepositoryImpl
import ru.esstu.features.messanger.supports.data.repository.SupportsUpdatesRepositoryImpl


fun supportsDataModule() = DI.Module("SupportsDataModule") {
    bind<SupportsApi>() with singleton {
        SupportsApiImpl(
            instance()
        )
    }

    bind<SupportsCacheDao>() with singleton {
        SupportsCacheDatabase(
            instance<IDatabaseStudent>().getDataBase().supportQueries
        )
    }


    bind<ISupportsApiRepository>() with singleton {
        SupportsApiRepositoryImpl(
            instance(),
        )
    }

    bind<ISupportsUpdatesRepository>() with singleton {
        SupportsUpdatesRepositoryImpl(
            instance(),
        )
    }
}