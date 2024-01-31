package ru.esstu.features.profile.porfolio.domain.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.features.profile.porfolio.data.di.portfolioDataModule
import ru.esstu.features.profile.porfolio.domain.repository.IPortfolioRepository
import ru.esstu.features.profile.porfolio.domain.repository.PortfolioRepositoryImpl


fun portfolioDomainModule() = DI.Module("PortfolioDomainModule") {
    importOnce(portfolioDataModule())

    bind<IPortfolioRepository>() with singleton {
        PortfolioRepositoryImpl(
            api = instance()
        )
    }
}