package ru.news.announcement.data.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.news.announcement.data.web.NewsApi
import ru.news.announcement.data.web.NewsApiImpl

val newsDi = DI.Module("NewsDi"){
    bind<NewsApi>() with singleton {
        NewsApiImpl(
            portalApi = instance()
        )
    }
}