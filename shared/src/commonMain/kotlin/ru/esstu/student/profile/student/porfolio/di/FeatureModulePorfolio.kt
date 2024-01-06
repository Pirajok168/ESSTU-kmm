package ru.esstu.student.profile.student.porfolio.di

import org.kodein.di.DI
import ru.esstu.student.profile.student.porfolio.domain.di.portfolioModule


internal val featureModulePortfolio = DI.Module("featureModulePortfolio") {
    importAll(
        portfolioModule
    )
}