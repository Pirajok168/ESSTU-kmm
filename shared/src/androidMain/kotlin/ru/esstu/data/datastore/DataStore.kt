package ru.esstu.data.datastore

import android.content.Context
import com.russhwolf.settings.Settings
import ru.esstu.ContextApplication


class AndroidSettings : DataStore {
    private var context: Context = ContextApplication.getContextApplication().context

    override val settings: Settings.Factory
        get() = com.russhwolf.settings.AndroidSettings.Factory(context = context)
}

actual fun getSettings(): DataStore = AndroidSettings()