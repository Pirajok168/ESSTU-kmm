package ru.esstu.android.authorized.navigation.bottom_navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import ru.esstu.android.R
import ru.esstu.android.authorized.messaging.messanger.MessengerScreens
import ru.esstu.android.authorized.messaging.messanger.messengerNavGraph
import ru.esstu.android.authorized.news.navigation.NewsScreens
import ru.esstu.android.authorized.news.navigation.newsNavGraph
import ru.esstu.android.authorized.student.profile.navigation.ProfileScreens
import ru.esstu.android.authorized.student.profile.navigation.profileNavGraph
import ru.esstu.android.domain.navigation.bottom_navigation.util.NavItem


@Composable
fun BottomAuthorizedNavigation(parentNavController: NavHostController) {
    BottomNavScreen(
        startDestination = NewsScreens.Root,
        navItems = listOf(
            NavItem(
                icon = R.drawable.ic_bottom_home,
                label = "Главная",
                route = NewsScreens.Root,
                badges = 0
            ),
            NavItem(
                icon = R.drawable.ic_bottom_chat,
                label = "Сообщения",
                route = MessengerScreens.Root,
                badges = 0
            ),

            NavItem(
                icon = R.drawable.ic_bottom_account,
                label = "Профиль",
                route = ProfileScreens.Root,
                badges = 0
            ),
        ), contentNavGraph = { padding, inlineNavController ->
            newsNavGraph(
                padding = padding,
                navController = inlineNavController,
                parentNavController = parentNavController
            )
            messengerNavGraph(
                padding = padding,
                NavController = inlineNavController,
                parentNavController = parentNavController
            )
            profileNavGraph(
                padding = padding,
                navController = inlineNavController,
                parentNavController = parentNavController,
            )
        }
    )
}