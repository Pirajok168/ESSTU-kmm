package ru.esstu.auth.datasources.repo

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import platform.Foundation.*

class AppleSettings() : DataStore{

    override val settings: Settings.Factory
        get() = AppleSettings.Factory()
}

actual fun getSettings(): DataStore =  AppleSettings()