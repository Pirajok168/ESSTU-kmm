package ru.esstu.di

import org.kodein.di.DI
import org.kodein.di.LazyDI
import ru.esstu.auth.domain.di.authorizationDomainModule
import ru.esstu.data.datastore.di.dataStoreDi
import ru.esstu.data.db.databaseModule
import ru.esstu.data.web.di.domainApi
import ru.esstu.data.web.di.ktorModule
import ru.esstu.features.account.domain.di.accountDomainModule
import ru.esstu.features.firebase.domain.di.firebaseDomainModule
import ru.esstu.features.update.data.di.moduleApiUpdates


val sharedDi: DI = LazyDI {
    DI {
        importOnce(ktorModule)
        importOnce(domainApi)
        importOnce(moduleApiUpdates)

        // TODO("УБРАТЬ ОБЯЗАТЕЛЬНО")
        importOnce(authorizationDomainModule())


        importOnce(firebaseDomainModule())
        importOnce(accountDomainModule())
        importOnce(dataStoreDi())
        importOnce(databaseModule())
    }
}
