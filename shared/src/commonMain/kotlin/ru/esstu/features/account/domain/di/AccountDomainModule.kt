package ru.esstu.features.account.domain.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.data.token.repository.LoginDataRepositoryImpl
import ru.esstu.features.account.data.di.accountDataModule
import ru.esstu.features.account.domain.repo.AccountInfoRepositoryImpl
import ru.esstu.features.account.domain.repo.IAccountInfoApiRepository


fun accountDomainModule() = DI.Module("AccountDomainModule") {
    importOnce(accountDataModule())

    bind<IAccountInfoApiRepository>() with singleton {
        AccountInfoRepositoryImpl(
            loginDataRepository = instance<LoginDataRepositoryImpl>(),
            userCache = instance(),
            api = instance(),
            instance()
        )
    }
}