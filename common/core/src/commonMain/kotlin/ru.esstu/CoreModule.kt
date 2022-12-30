package ru.esstu

import org.kodein.di.DI
import ru.esstu.json.serializationModule
import ru.esstu.ktor.domainApi

val coreModule = DI.Module("coreModule") {
    importAll(
        serializationModule,
        domainApi,


    )
}