package ru.esstu.auth.datasources





import org.kodein.di.DI
import ru.esstu.auth.datasources.di.authProvidesModule



internal val featureModuleAuth = DI.Module(name = "featureModuleAuth"){
    importAll(
        authProvidesModule,
    )
}