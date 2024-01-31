package ru.esstu.data.datastore.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton
import ru.esstu.data.datastore.AccountInfoDSManager
import ru.esstu.data.datastore.IAccountInfoDSManager
import ru.esstu.data.datastore.create

fun dataStoreDi() = DI.Module("DataStoreDi") {
    bind<IAccountInfoDSManager>() with singleton {
        AccountInfoDSManager(
            dataStore = create()
        )
    }
}