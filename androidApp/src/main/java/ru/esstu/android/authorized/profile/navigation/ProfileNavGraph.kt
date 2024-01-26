package ru.esstu.android.authorized.profile.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.esstu.android.authorized.profile.portfolio.ui.AttestationScreen
import ru.esstu.android.authorized.profile.portfolio.ui.PortfolioScreen
import ru.esstu.android.authorized.profile.portfolio.viewmodel.PortfolioViewModel
import ru.esstu.android.authorized.profile.ui.ProfileScreen

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
            ProfileScreen(paddingValues = padding, onNavigateAttestation = {
                navController.navigate(ProfileScreens.Attestation.navigate())
            } ){
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

        composable(
            route  = ProfileScreens.Attestation.passRoute(),
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
            AttestationScreen(
                paddingValues = padding,
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
    }
}