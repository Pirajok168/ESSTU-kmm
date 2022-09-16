package ru.esstu.domain.ktor

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

internal val ktorModule  = DI.Module(
    name = "ktorModule",
    init = {
        bind<HttpEngineFactory>() with singleton { HttpEngineFactory() }
    }
)