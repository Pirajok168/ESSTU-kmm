package ru.esstu.domain

import org.kodein.di.DI
import ru.esstu.data.web.di.domainApi
import ru.esstu.data.web.di.ktorModule
import ru.esstu.features.update.data.di.moduleApiUpdates


internal val featureModuleDomain = DI.Module("FeatureModuleDomain"){
    importAll(
        ktorModule,
        domainApi,


        moduleApiUpdates,

    )
}