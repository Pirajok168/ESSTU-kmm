package ru.esstu.features.news.announcement.domain.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.features.news.announcement.data.di.announcementsDataModule
import ru.esstu.features.news.announcement.domain.repo.AnnouncementsRepositoryImpl
import ru.esstu.features.news.announcement.domain.repo.IAnnouncementsRepository


fun announcementsDomainModule() = DI.Module("announcementsDomainModule") {
    importOnce(announcementsDataModule())

    bind<IAnnouncementsRepository>() with singleton {
        AnnouncementsRepositoryImpl(
            newsApi = instance(),
            newsDao = instance()
        )
    }

}