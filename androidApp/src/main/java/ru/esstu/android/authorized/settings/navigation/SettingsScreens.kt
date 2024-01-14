package ru.esstu.android.authorized.settings.navigation

import androidx.navigation.NavType
import ru.esstu.android.domain.navigation.Route



sealed class SettingsScreens<T>(
    route: String? = null,
    navArgs: Map<T, NavType<*>> = emptyMap()
) : Route<T>(route, navArgs) {
    data object Root : SettingsScreens<Unit>()

    data object Main : SettingsScreens<Unit>()

    data object LocaleApp : SettingsScreens<Unit>()
}
