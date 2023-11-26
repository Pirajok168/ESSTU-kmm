package ru.esstu.android.authorized.student.profile.navigation

import androidx.navigation.NavType
import ru.esstu.android.domain.navigation.Route

sealed class ProfileScreens<T>(
    route: String? = null,
    navArgs: Map<T, NavType<*>> = emptyMap()
) : Route<T>(route, navArgs) {
    object Root : ProfileScreens<Unit>()

    object Profile: ProfileScreens<Unit>()
}
