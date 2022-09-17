package ru.esstu.student.news

import org.kodein.di.DI
import ru.esstu.student.news.announcement.di.announcementsModule


internal val featureModuleNews = DI.Module("featureModuleNews") {
    importAll(
        announcementsModule,
    )

}