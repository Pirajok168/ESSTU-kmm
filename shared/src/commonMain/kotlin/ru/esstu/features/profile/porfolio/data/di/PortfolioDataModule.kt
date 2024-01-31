package ru.esstu.features.profile.porfolio.data.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.data.fileSystem.storage
import ru.esstu.features.profile.porfolio.data.api.PortfolioApi
import ru.esstu.features.profile.porfolio.data.api.PortfolioApiImpl


fun portfolioDataModule() = DI.Module("PortfolioDataModule") {
    bind<PortfolioApi>() with singleton {
        PortfolioApiImpl(
            authorizedApi = instance(),
            fileSystem = storage().fileSystem
        )
    }
}