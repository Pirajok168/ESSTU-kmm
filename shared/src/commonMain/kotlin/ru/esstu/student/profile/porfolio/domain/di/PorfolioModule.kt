package ru.esstu.student.profile.porfolio.domain.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.domain.modules.account.datasources.datastore.storage
import ru.esstu.student.profile.porfolio.data.api.PortfolioApi
import ru.esstu.student.profile.porfolio.data.api.PortfolioApiImpl
import ru.esstu.student.profile.porfolio.domain.repository.IPortfolioRepository
import ru.esstu.student.profile.porfolio.domain.repository.PortfolioRepositoryImpl
import kotlin.native.concurrent.ThreadLocal

internal val portfolioModule = DI.Module("PorfolioModule"){
    bind<PortfolioApi>() with singleton {
        PortfolioApiImpl(
            authorizedApi = instance(),
            fileSystem = storage().fileSystem
        )
    }

    bind<IPortfolioRepository>() with singleton {
        PortfolioRepositoryImpl(
            api = instance()
        )
    }
}

@ThreadLocal
object PortfolioModule{
    val repo: IPortfolioRepository
        get() = ESSTUSdk.di.instance()

}


val ESSTUSdk.portfolioModule: PortfolioModule
    get() = PortfolioModule