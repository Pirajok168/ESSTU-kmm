package ru.esstu.android.domain.navigation.bottom_navigation.util

import androidx.compose.ui.graphics.Color
import ru.esstu.android.domain.navigation.Route

data class NavItem(
    val icon: IconResource,
    val label: String,
    val badges: Int = 0,
    val color:Color? = null,
    val route: Route<*>
)

