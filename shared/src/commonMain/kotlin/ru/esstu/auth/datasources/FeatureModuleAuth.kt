package ru.esstu.auth.datasources





import org.kodein.di.DI
import ru.esstu.auth.datasources.di.authProvidesModule



val featureModuleAuth = DI.Module(name = "featureModuleAuth"){
    importAll(
        authProvidesModule,
    )
}