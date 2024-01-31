package ru.esstu.features.account.data.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.features.account.data.api.AccountInfoApi
import ru.esstu.features.account.data.api.AccountInfoApiImpl

fun accountDataModule() = DI.Module("accountDataModule") {
    bind<AccountInfoApi>() with singleton {
        AccountInfoApiImpl(
            instance()
        )
    }
}