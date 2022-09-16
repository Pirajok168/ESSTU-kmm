package ru.esstu.android.domain.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
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
import ru.esstu.android.student.navigation.StudentRoutes
import ru.esstu.android.student.navigation.studentNavGraph


@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    logoutViewModel: LogoutViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        logoutViewModel.logoutFlow.collectLatest {
            navController.backQueue.clear()
            navController.navigate(AuthRoutes.LoginScreen.passRoute())
        }
    }
    NavHost(navController = navController, startDestination = AuthRoutes.Root.startDest()) {
        authNavGraph(
            navController = navController,
          //  onNavToTeacher = { navController.navigate(TeacherRoutes.Root.passRoute()) },
            onNavToStudent = { navController.navigate(StudentRoutes.Root.passRoute()) },
            onNavToEntrant = { /* TODO */ },
            onNavToGuest = { /* TODO */ },
        )
        studentNavGraph(navController)
       /* //TODO не настроен
        guestNavGraph(navController)

        //TODO не настроен
        entrantNavGraph(navController)

        studentNavGraph(navController)

        teacherNavGraph(navController)

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
        }*/
    }
}