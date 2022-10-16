package ru.esstu.domain.modules.account.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.auth.datasources.di.AuthModule
import ru.esstu.domain.modules.account.datasources.api.AccountInfoApi
import ru.esstu.domain.modules.account.datasources.api.AccountInfoApiImpl
import ru.esstu.domain.modules.account.datasources.datastore.AccountInfoDSManager
import ru.esstu.domain.modules.account.datasources.datastore.IAccountInfoDSManager
import ru.esstu.domain.modules.account.datasources.datastore.create
import ru.esstu.domain.modules.account.datasources.datastore.producePath
import ru.esstu.domain.modules.account.datasources.repo.AccountInfoRepositoryImpl
import ru.esstu.domain.modules.account.datasources.repo.IAccountInfoApiRepository
import kotlin.native.concurrent.ThreadLocal

internal val accountModule = DI.Module("AccountModule"){
    bind<AccountInfoApi>() with singleton {
        AccountInfoApiImpl(
            portalApi = instance()
        )
    }

    bind<IAccountInfoDSManager>() with singleton {
        AccountInfoDSManager(
            dataStore = create()
        )
    }

    bind<IAccountInfoApiRepository>() with singleton {
        AccountInfoRepositoryImpl(
            userCache = instance(),
            api = instance(),
            auth = instance()
        )
    }
}

@ThreadLocal
object AccountModule {
    val repo: IAccountInfoApiRepository
        get() = ESSTUSdk.di.instance()
}

val ESSTUSdk.accountModule: AccountModule
    get() = AccountModule