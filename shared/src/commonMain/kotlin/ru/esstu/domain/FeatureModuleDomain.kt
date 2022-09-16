package ru.esstu.domain

import org.kodein.di.DI
import ru.esstu.auth.datasources.featureModuleAuth
import ru.esstu.domain.ktor.domainApi
import ru.esstu.domain.ktor.ktorModule

val featureModuleDomain = DI.Module("FeatureModuleDomain"){
    importAll(
        ktorModule,
        domainApi,
        featureModuleAuth,

    )
}