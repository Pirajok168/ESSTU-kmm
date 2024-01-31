package ru.esstu.features.update.data.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.features.update.data.api.UpdatesApi
import ru.esstu.features.update.data.api.UpdatesApiImpl

internal val moduleApiUpdates = DI.Module("ModuleApiUpdates") {
    bind<UpdatesApi>() with singleton {
        UpdatesApiImpl(
            portalApi = instance()
        )
    }

}