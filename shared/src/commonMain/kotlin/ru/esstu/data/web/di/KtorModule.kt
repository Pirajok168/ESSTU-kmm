package ru.esstu.data.web.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton
import ru.esstu.data.web.api.HttpEngineFactory

internal val ktorModule = DI.Module(
    name = "ktorModule",
    init = {
        bind<HttpEngineFactory>() with singleton { HttpEngineFactory() }
    }
)