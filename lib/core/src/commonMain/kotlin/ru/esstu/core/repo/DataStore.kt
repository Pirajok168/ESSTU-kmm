package ru.esstu.core.repo

import com.russhwolf.settings.Settings

interface DataStore {
    val settings: Settings.Factory
}

expect fun getSettings(): DataStore

