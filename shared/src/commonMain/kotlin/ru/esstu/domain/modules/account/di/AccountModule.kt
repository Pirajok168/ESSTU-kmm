package ru.esstu.domain.modules.account.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.auth.datasources.local.LoginDataRepositoryImpl
import ru.esstu.domain.modules.account.datasources.api.AccountInfoApi
import ru.esstu.domain.modules.account.datasources.api.AccountInfoApiImpl
import ru.esstu.domain.modules.account.datasources.datastore.AccountInfoDSManager
import ru.esstu.domain.modules.account.datasources.datastore.IAccountInfoDSManager
import ru.esstu.domain.modules.account.datasources.datastore.create
import ru.esstu.domain.modules.account.datasources.datastore.storage
import ru.esstu.domain.modules.account.datasources.repo.AccountInfoRepositoryImpl
import ru.esstu.domain.modules.account.datasources.repo.IAccountInfoApiRepository
import ru.esstu.domain.modules.downloader.Downloader
import ru.esstu.domain.modules.downloader.IDownloader
import kotlin.native.concurrent.ThreadLocal

internal val accountModule = DI.Module("AccountModule"){
    bind<AccountInfoApi>() with singleton {
        AccountInfoApiImpl(
            authorizedApi = instance()
        )
    }

    bind<IAccountInfoDSManager>() with singleton {
        AccountInfoDSManager(
            dataStore = create()
        )
    }

    bind<IAccountInfoApiRepository>() with singleton {
        AccountInfoRepositoryImpl(
            loginDataRepository = instance<LoginDataRepositoryImpl>(),
            userCache = instance(),
            api = instance(),
            instance()
        )
    }

    bind<IDownloader>() with singleton {
        Downloader(
            fileSystem = storage().fileSystem,
            instance()
        )
    }
}

@ThreadLocal
object AccountModule {
    val repo: IAccountInfoApiRepository
        get() = ESSTUSdk.di.instance()

    val download: IDownloader
        get() = ESSTUSdk.di.instance()
}

val ESSTUSdk.accountModule: AccountModule
    get() = AccountModule