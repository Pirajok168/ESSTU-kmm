package ru.esstu.domain.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.domain.api.UpdatesApi
import ru.esstu.domain.api.UpdatesApiImpl

internal val moduleApiUpdates = DI.Module("ModuleApiUpdates") {
    bind<UpdatesApi>() with singleton {
        UpdatesApiImpl(
            portalApi = instance()
        )
    }

}