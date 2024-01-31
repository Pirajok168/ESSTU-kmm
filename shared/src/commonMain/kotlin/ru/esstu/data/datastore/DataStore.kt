package ru.esstu.data.datastore

import com.russhwolf.settings.Settings

interface DataStore {
    val settings: Settings.Factory
}

expect fun getSettings(): DataStore