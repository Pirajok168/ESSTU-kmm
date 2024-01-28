package ru.esstu.android.domain.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.flow.collectLatest
import ru.esstu.android.auth.navigation.AuthRoutes
import ru.esstu.android.auth.navigation.authNavGraph
import ru.esstu.android.auth.viewmodel.LogoutViewModel
import ru.esstu.android.authorized.navigation.authorizedNavigationGraph
import ru.esstu.android.domain.modules.image_viewer.navigation.ImageScreen
import ru.esstu.android.domain.modules.image_viewer.navigation.ImageScreenArguments
import ru.esstu.android.domain.modules.image_viewer.ui.ImageScreen
import ru.esstu.android.domain.viewmodel.AppErrorViewModel
import ru.esstu.android.student.navigation.StudentRoutes
import ru.esstu.domain.handleError.AppError


@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    logoutViewModel: LogoutViewModel = viewModel(),
    appErrorViewModel: AppErrorViewModel= viewModel(),
) {

    appErrorViewModel.state.errorProcessor?.let {
        if (it == AppError.Uncheck) {
            appErrorViewModel.onResetError()
            return@let
        }
        AppErrorDialog(appError = it, onResetError = {
            appErrorViewModel.onResetError()
            if (it == AppError.Unauthorized){
                navController.navigate(AuthRoutes.LoginScreen.passRoute()){
                    popUpTo(navController.currentDestination?.route!!) {
                        inclusive = true
                    }
                }
            }
        })
    }

    LaunchedEffect(Unit) {
        logoutViewModel.logoutFlow.collectLatest {
           navController.navigate(AuthRoutes.LoginScreen.passRoute()){
               popUpTo(navController.currentDestination?.route!!) {
                   inclusive = true
               }
           }
        }
    }
    NavHost(navController = navController, startDestination = AuthRoutes.Root.startDest()) {
        authNavGraph(
            navController = navController,
            onNavToStudent = {
                navController.navigate(StudentRoutes.Root.passRoute()){
                    popUpTo(AuthRoutes.Root.startDest()) {
                        inclusive = true
                    }
                }
            },
            onNavToEntrant = {

            },
            onNavToGuest = {
                navController.navigate(StudentRoutes.Root.passRoute())
            },
            onNavToTeacher =  {
                navController.navigate(StudentRoutes.Root.passRoute()){
                    popUpTo(AuthRoutes.Root.startDest()) {
                        inclusive = true
                    }
                }
            }
        )

        authorizedNavigationGraph(navController)



        //крашит, если ссылаться на этот экран в studentNavGraph (хз почему)
        composable(route = ImageScreen.passRoute(), arguments = ImageScreen.passArguments()) {
            val startImage = it.arguments?.getString(ImageScreenArguments.START_IMAGE.name)
            val images = it.arguments?.getString(ImageScreenArguments.IMAGES.name)

            val imagesArr = images
                ?.removePrefix("[")
                ?.removeSuffix("]")
                ?.split(", ")

            if (!imagesArr.isNullOrEmpty())
               ImageScreen(
                    imageUris = imagesArr,
                    startImage = startImage.orEmpty(),
                    onBackPress = { navController.popBackStack() }
                )
        }
    }
}

@Composable
private fun AppErrorDialog(appError: AppError, onResetError: () -> Unit) {
    AlertDialog(
        onDismissRequest = onResetError,
        title = {
            Text(text = "Произошла ошибка")
        },
        text = {
            Text(text = if (appError == AppError.Unauthorized) "Авторизуйтесь в приложении повторно" else "Ошибка сервера. Повторите попытку позже")
        },
        confirmButton = {
            TextButton(
                onClick = onResetError
            ) {
                Text("Ок")
            }
        },
        dismissButton = {
            if (appError != AppError.Unauthorized){
                TextButton(
                    onClick = onResetError
                ) {
                    Text("Отмена")
                }
            }
        }
    )
}