package ru.esstu.domain

import org.kodein.di.DI
import ru.esstu.auth.datasources.featureModuleAuth
import ru.esstu.domain.di.moduleApiUpdates
import ru.esstu.domain.ktor.domainApi
import ru.esstu.domain.ktor.ktorModule
import ru.esstu.domain.modules.account.di.accountModule


internal val featureModuleDomain = DI.Module("FeatureModuleDomain"){
    importAll(
        ktorModule,
        domainApi,
        featureModuleAuth,
        moduleApiUpdates,
        accountModule,

    )
}