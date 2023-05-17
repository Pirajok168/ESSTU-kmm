package ru.esstu.android.student.navigation

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import ru.esstu.android.R

import ru.esstu.android.domain.navigation.bottom_navigation.BottomNavScreen
import ru.esstu.android.domain.navigation.bottom_navigation.util.IconResource
import ru.esstu.android.domain.navigation.bottom_navigation.util.NavItem
import ru.esstu.android.student.messaging.messenger.navigation.MessengerScreens
import ru.esstu.android.student.messaging.messenger.navigation.messengerNavGraph
import ru.esstu.android.student.news.navigation.NewsScreens
import ru.esstu.android.student.news.navigation.newsNavGraph


@Composable
fun StudentBottomNavScreen(parentNavController: NavHostController) {
    BottomNavScreen(
        modifier = Modifier.navigationBarsPadding(),
        startDestination = NewsScreens.Root,
        navItems = arrayOf(
            NavItem(
                icon = IconResource.DrawableResource(R.drawable.ic_bottom_home),
                label = "Главная",
                route = NewsScreens.Root,
                badges = 0
            ),
            NavItem(
                icon = IconResource.DrawableResource(R.drawable.ic_bottom_chat),
                label = "Сообщения",
                route = MessengerScreens.Root,
                badges = 0
            ),
            /*NavItem(
                icon = IconResource.DrawableResource(R.drawable.ic_bottom_home),
                label = "Главная",
                route = NewsScreens.Root,
                badges = 0
            ),
            NavItem(
                icon = IconResource.DrawableResource(R.drawable.ic_bottom_map),
                route = MapScreen,
                label = "Карта",
                color = MaterialTheme.colors.background.copy(alpha = 0.8f),
                badges = 0
            ),
            NavItem(
                icon = IconResource.DrawableResource(R.drawable.ic_bottom_calendar),
                label = "Расписание",
                route = ScheduleScreen,
                badges = 0
            ),
            NavItem(
                icon = IconResource.DrawableResource(R.drawable.ic_bottom_chat),
                label = "Сообщения",
                route = MessengerScreens.Root,
                badges = 0
            ),
            NavItem(
                icon = IconResource.DrawableResource(R.drawable.ic_bottom_account),
                label = "Профиль",
                route = StudentProfileScreens.Root,
                badges = 0
            ),*/
        ), contentNavGraph = { padding, inlineNavController ->
            newsNavGraph(padding = padding, navController = inlineNavController, parentNavController = parentNavController)
            messengerNavGraph(padding = padding, NavController = inlineNavController, parentNavController = parentNavController)
            /*composable(route = MapScreen.passRoute()) {
                MapScreen(parentPadding = padding)
            }

            composable(route = ScheduleScreen.passRoute()) {
                Schedule(padding = padding)
            }



            profileNavGraph(padding = padding, navController = inlineNavController, parentNavController = parentNavController)*/
        })
}