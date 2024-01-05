package ru.esstu.android.authorized.student.profile.navigation

import android.util.Log
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.coroutines.launch
import ru.esstu.ESSTUSdk
import ru.esstu.android.authorized.messaging.dialog_chat.navigation.DialogChatScreen
import ru.esstu.android.authorized.messaging.group_chat.navigation.GroupChatScreen
import ru.esstu.android.authorized.messaging.new_message.selector.navigation.SelectorScreens
import ru.esstu.android.authorized.news.navigation.NewsScreens
import ru.esstu.android.authorized.student.profile.portfolio.ui.PortfolioScreen
import ru.esstu.android.authorized.student.profile.portfolio.viewmodel.PortfolioViewModel
import ru.esstu.android.authorized.student.profile.ui.StudentProfileScreen
import ru.esstu.student.profile.student.porfolio.domain.di.portfolioModule
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioType
import ru.esstu.student.profile.student.porfolio.domain.repository.IPortfolioRepository

fun NavGraphBuilder.profileNavGraph(
    parentNavController: NavHostController,
    navController: NavHostController,
    padding: PaddingValues
) {
    navigation(
        route = ProfileScreens.Root.passRoute(),
        startDestination = ProfileScreens.Profile.startDest()
    ) {
        composable(
            route = ProfileScreens.Profile.passRoute(),
        ) {
            val portfolioViewModel: PortfolioViewModel = viewModel()
            StudentProfileScreen(padding){
                portfolioViewModel.preDisplayFile(it)
                navController.navigate(ProfileScreens.Portfolio.navigate())
            }
        }

        composable(
            route  = ProfileScreens.Portfolio.passRoute(),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = {
                        it / 2
                    }
                )
            },
            exitTransition = {
                slideOutHorizontally {
                    it / 2
                }
            }
        ){
            val parent = remember(it) {
                navController.getBackStackEntry(ProfileScreens.Profile.popTo())
            }
            PortfolioScreen(
                paddingValues = padding,
                portfolioViewModel = viewModel(parent),
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
    }
}