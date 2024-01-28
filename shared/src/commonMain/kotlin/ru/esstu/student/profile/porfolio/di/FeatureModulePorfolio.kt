package ru.esstu.student.profile.porfolio.di

import org.kodein.di.DI
import ru.esstu.student.profile.porfolio.domain.di.portfolioModule


internal val featureModulePortfolio = DI.Module("featureModulePortfolio") {
    importAll(
        portfolioModule
    )
}