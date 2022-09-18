package ru.esstu.student.news.announcement.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.student.news.announcement.datasources.api.NewsApi
import ru.esstu.student.news.announcement.datasources.api.NewsApiImpl
import ru.esstu.student.news.announcement.datasources.db.timestamp.DatabaseTimestamp
import ru.esstu.student.news.announcement.datasources.db.timestamp.TimestampDao
import ru.esstu.student.news.announcement.datasources.db.timestamp.driverTimestampFactory
import ru.esstu.student.news.announcement.datasources.repo.AnnouncementsRepositoryImpl
import ru.esstu.student.news.announcement.datasources.repo.AnnouncementsUpdateRepositoryImpl
import ru.esstu.student.news.announcement.datasources.repo.IAnnouncementsRepository
import ru.esstu.student.news.announcement.datasources.repo.IAnnouncementsUpdateRepository
import ru.esstu.student.news.announcement.db.Database
import ru.esstu.student.news.announcement.db.driverNewsFactory
import ru.esstu.student.news.datasources.NewsDao

import kotlin.native.concurrent.ThreadLocal

internal val announcementsModule = DI.Module("AnnouncementsModule") {
    bind<NewsApi>() with singleton {
        NewsApiImpl(
            portalApi = instance()
        )
    }

    bind<NewsDao>() with singleton {
        val driverFactory = driverNewsFactory()
        Database(
            databaseNewsFactory = driverFactory.sqlDriver
        )
    }

    bind<TimestampDao>() with singleton {
        val driverTimestampFactory = driverTimestampFactory()
        DatabaseTimestamp(
            databaseTimestampFactory = driverTimestampFactory.sqlDriver
        )
    }

    bind<IAnnouncementsRepository>() with singleton{
        AnnouncementsRepositoryImpl(
            auth = instance(),
            newsApi = instance(),
            newsDao = instance()
        )
    }

    bind<IAnnouncementsUpdateRepository>() with singleton {
        AnnouncementsUpdateRepositoryImpl(
            auth = instance(),
            api = instance(),
            timestampDao = instance()
        )
    }

}


@ThreadLocal
object AnnouncementsModule{
    val repo: IAnnouncementsRepository
        get() = ESSTUSdk.di.instance()

    val updates: IAnnouncementsUpdateRepository
        get() = ESSTUSdk.di.instance()
}


val ESSTUSdk.announcementsModule: AnnouncementsModule
    get() = AnnouncementsModule