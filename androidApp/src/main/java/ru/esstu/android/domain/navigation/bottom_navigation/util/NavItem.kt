package ru.esstu.android.domain.navigation.bottom_navigation.util

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import ru.esstu.android.domain.navigation.Route

data class NavItem(
    @DrawableRes val icon: Int,
    val label: String,
    val badges: Int = 0,
    val color:Color? = null,
    val route: Route<*>
)

