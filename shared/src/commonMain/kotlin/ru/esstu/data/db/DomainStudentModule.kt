package ru.esstu.data.db

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton


fun databaseModule(): DI.Module =
    DI.Module("databaseModule") {
        bind<IDatabaseStudent>() with singleton {
            val driverFactory = databaseFactory()
            DatabaseStudent(
                sqlDriver = driverFactory.sqlDriver
            )
        }
    }
