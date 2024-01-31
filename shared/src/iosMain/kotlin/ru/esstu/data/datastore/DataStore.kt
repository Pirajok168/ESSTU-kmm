package ru.esstu.data.datastore

import com.russhwolf.settings.Settings


class AppleSettings : DataStore {

    override val settings: Settings.Factory
        get() = com.russhwolf.settings.AppleSettings.Factory()
}


actual fun getSettings(): DataStore = AppleSettings()