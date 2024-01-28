package ru.esstu.android.authorized.profile.navigation

import androidx.navigation.NavType
import ru.esstu.android.domain.navigation.Route

sealed class ProfileScreens<T>(
    route: String? = null,
    navArgs: Map<T, NavType<*>> = emptyMap()
) : Route<T>(route, navArgs) {
    data object Root : ProfileScreens<Unit>()

    data object Profile: ProfileScreens<Unit>()

    data object StudentPortfolio: ProfileScreens<Unit>()

    data object EmployeePortfolio: ProfileScreens<Unit>()

    data object Attestation: ProfileScreens<Unit>()
}
