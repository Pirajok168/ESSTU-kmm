package ru.esstu.android.auth.navigation


import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import ru.esstu.android.auth.ui.*


fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    onNavToGuest: () -> Unit = { },
    onNavToEntrant: () -> Unit = { },
    onNavToStudent: () -> Unit = { },
    onNavToTeacher: () -> Unit = { },
) {

    fun popToStartDest(onNavToDest: () -> Unit) {

        //Todo("")  navController.backQueue.clear()
        onNavToDest()
    }

    navigation(
        startDestination = AuthRoutes.Splashscreen.startDest(),
        route = AuthRoutes.Root.passRoute()
    ) {

        composable(route = AuthRoutes.Splashscreen.passRoute()) {

            val context = LocalContext.current

            Splashscreen(
                /*onNavToAuth = {
                    navController.navigate(AuthRoutes.LoginChooseScreen.navigate()) {
                        popUpTo(AuthRoutes.Splashscreen.popTo()) {
                            inclusive = true
                        }
                    }
                },*/
                onNavToGuest = { popToStartDest(onNavToGuest) },
                onNavToEntrant = { popToStartDest(onNavToEntrant) },
                onNavToStudent = { popToStartDest(onNavToStudent) },
                onNavToTeacher = { popToStartDest(onNavToTeacher) },
                onFinishApp = { (context as? Activity)?.finish() }
            )
        }

        composable(route = AuthRoutes.LoginChooseScreen.passRoute()) {
            LoginChooseScreen(
                onNavToEntrant = {
                    navController.navigate(AuthRoutes.EntrantLoginChooseScreen.navigate())
                },
                onNavToAuth = {
                    navController.navigate(AuthRoutes.LoginScreen.navigate())
                })
        }

        composable(route = AuthRoutes.LoginScreen.passRoute()) {
            LoginScreen(
                onBackPressed = { navController.popBackStack() },
                onNavToPass = {
                    navController.navigate(AuthRoutes.PasswordScreen.navigate())
                }
            )
        }

        composable(route = AuthRoutes.PasswordScreen.passRoute()) {

            val parent = remember {
                navController.getBackStackEntry(AuthRoutes.LoginScreen.popTo())
            }

            PasswordScreen(
                onBackPressed = { navController.popBackStack() },
                onNavToEntrantFlow = { popToStartDest(onNavToEntrant) },
                onNavToStudentFlow = { popToStartDest(onNavToStudent) },
                onNavToTeacherFlow = { popToStartDest(onNavToTeacher) },
                authViewModel = viewModel(parent)
            )
        }

        composable(route = AuthRoutes.EntrantLoginChooseScreen.passRoute()) {
            EntrantLoginChooseScreen(
                onBackPressed = { navController.popBackStack() },
                onNavToGuest = { popToStartDest(onNavToGuest) },
                onNavToEntrant = {
                    navController.navigate(AuthRoutes.EntrantLoginScreen.navigate())
                })
        }

        composable(route = AuthRoutes.EntrantLoginScreen.passRoute()) {

            EntrantLoginScreen(onBackPressed = { navController.popBackStack() },
                onNavToPass = {
                    navController.navigate(AuthRoutes.EntrantPasswordScreen.navigate())
                })
        }

        composable(route = AuthRoutes.EntrantPasswordScreen.passRoute()) {

            val parent = remember {
                navController.getBackStackEntry(AuthRoutes.EntrantLoginScreen.popTo())
            }

            EntrantPasswordScreen(
                onBackPressed = { navController.popBackStack() },
                onNavToEntrantFlow = { popToStartDest(onNavToEntrant) },
                authViewModel = viewModel(parent)
            )
        }
    }
}