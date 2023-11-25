package ru.esstu.android.authorized.messaging.messanger

import androidx.navigation.NavType
import ru.esstu.android.domain.navigation.Route

sealed class MessengerScreens<T>(
    route: String? = null,
    navArgs: Map<T, NavType<*>> = emptyMap()
) : Route<T>(route, navArgs) {
    object Root : MessengerScreens<Unit>()
    object Messenger : MessengerScreens<Unit>()
    object SearchScreen : MessengerScreens<Unit>()
}
