package ru.esstu.android.authorized.news.navigation

import androidx.navigation.NavType
import ru.esstu.android.domain.navigation.Route

sealed class NewsScreens<T>(route: String? = null, navArgs: Map<T, NavType<*>> = emptyMap()) : Route<T>(route, navArgs) {
    object Root : NewsScreens<Unit>()
    object SelectorScreen : NewsScreens<Unit>()
    object DetailScreen : NewsScreens<Unit>()
    object NewsScreen : NewsScreens<Unit>()
    object AnnouncementsScreen : NewsScreens<Unit>()
    object EventsScreen : NewsScreens<Unit>()
}

