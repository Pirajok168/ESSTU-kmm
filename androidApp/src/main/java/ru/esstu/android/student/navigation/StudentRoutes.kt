package ru.esstu.android.student.navigation

import androidx.navigation.NavType
import ru.esstu.android.domain.navigation.Route

sealed class StudentRoutes<T>(route: String? = null, navArgs: Map<T, NavType<*>> = emptyMap()) : Route<T>(route, navArgs) {
    data object Root : StudentRoutes<Unit>()
    data object BottomNavScreen : StudentRoutes<Unit>()
}