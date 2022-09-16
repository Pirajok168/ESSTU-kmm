package ru.esstu.android.auth.navigation

import androidx.navigation.NavType
import ru.esstu.android.domain.navigation.Route

sealed class AuthRoutes<T>(route: String? = null, navArgs: Map<T, NavType<*>> = emptyMap()) : Route<T>(route, navArgs) {
    object Root : AuthRoutes<Unit>("auth_root")
    object Splashscreen : AuthRoutes<Unit>()
    object LoginChooseScreen : AuthRoutes<Unit>()
    object LoginScreen : AuthRoutes<Unit>()
    object PasswordScreen : AuthRoutes<Unit>()
    object EntrantLoginChooseScreen : AuthRoutes<Unit>()
    object EntrantLoginScreen : AuthRoutes<Unit>()
    object EntrantPasswordScreen : AuthRoutes<Unit>()
}
