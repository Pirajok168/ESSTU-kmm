package ru.esstu.features.news.announcement.data.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.data.db.IDatabaseStudent
import ru.esstu.features.news.announcement.data.api.NewsApi
import ru.esstu.features.news.announcement.data.api.NewsApiImpl
import ru.esstu.features.news.announcement.data.db.Database
import ru.esstu.features.news.announcement.data.db.NewsDao


fun announcementsDataModule() = DI.Module("AnnouncementsDataModule") {
    bind<NewsApi>() with singleton {
        NewsApiImpl(
            authorizedApi = instance()
        )
    }

    bind<NewsDao>() with singleton {
        Database(
            database = instance<IDatabaseStudent>().getDataBase()
        )
    }
}