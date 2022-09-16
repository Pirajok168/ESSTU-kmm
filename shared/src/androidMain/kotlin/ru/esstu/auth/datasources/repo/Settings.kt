package ru.esstu.auth.datasources.repo

import android.content.Context
import android.content.SharedPreferences
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import ru.esstu.ContextApplication

class AndroidSettings() : DataStore{
    private  var context: Context = ContextApplication.getContextApplication().context

    override val settings: Settings.Factory
        get() = AndroidSettings.Factory(context = context)
}

actual fun getSettings(): DataStore =  AndroidSettings()