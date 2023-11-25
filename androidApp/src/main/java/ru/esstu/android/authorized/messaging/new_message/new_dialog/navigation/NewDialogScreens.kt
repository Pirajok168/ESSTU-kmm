package ru.esstu.android.authorized.messaging.new_message.new_dialog.navigation

import androidx.navigation.NavType
import ru.esstu.android.domain.navigation.Route

sealed class NewDialogScreens<T>(
    route: String? = null,
    navArgs: Map<T, NavType<*>> = emptyMap()
) : Route<T>(route, navArgs) {
    object Root : NewDialogScreens<Unit>()
    object NewDialogScreen : NewDialogScreens<Unit>()
    object SearchScreen : NewDialogScreens<Unit>()
}
