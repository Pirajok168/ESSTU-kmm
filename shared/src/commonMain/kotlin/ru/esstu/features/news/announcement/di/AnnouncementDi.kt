package ru.esstu.features.news.announcement.di

import org.kodein.di.DI
import ru.esstu.di.sharedDi
import ru.esstu.features.news.announcement.domain.di.announcementsDomainModule


fun announcementDi() = DI {
    extend(sharedDi)
    importOnce(announcementsDomainModule())
}