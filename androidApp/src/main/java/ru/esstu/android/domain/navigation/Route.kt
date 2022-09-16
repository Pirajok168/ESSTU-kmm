package ru.esstu.android.domain.navigation

import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink


abstract class Route<T>(route: String? = null, private val navArgs: Map<T, NavType<*>> = emptyMap()) {
    companion object {
        const val DEEP_LINK_SCHEME = "android"
        const val DEEP_LINK_HOST = "esstu.ru"
    }

    fun passDeepLinkRoute(): List<NavDeepLink> = listOf(navDeepLink {
        uriPattern = "$DEEP_LINK_SCHEME://$DEEP_LINK_HOST/$provideRoute${navArgs.map { "{${it.key}}" }.joinToString(separator = "/", prefix = if (navArgs.any()) "/" else "")}"
    })

    fun navigateDeepLink(args: (key: T) -> Any? = { null }):String = "$DEEP_LINK_SCHEME://$DEEP_LINK_HOST/$provideRoute${navArgs.mapNotNull { args(it.key) }.joinToString(separator = "/", prefix = if (navArgs.any()) "/" else "")}"

    private val provideRoute: String = route ?: this::class.java.name

    fun passRoute(): String = provideRoute + navArgs.map { "${it.key}={${it.key}}" }.joinToString(separator = "&", prefix = if (navArgs.any()) "?" else "")
    fun passArguments() = navArgs.map { navArgument(it.key.toString()) { type = it.value; nullable = true; defaultValue = null } }

    fun navigate(args: (key: T) -> Any? = { null }): String = provideRoute + navArgs.mapNotNull { args(it.key)?.let { arg -> "${it.key}=$arg" } }.let { it.joinToString(separator = "&", prefix = if (it.any()) "?" else "") }
    fun popTo() = provideRoute
    fun startDest() = provideRoute
}


//region SAMPLE: пример использования этого класса-навигатора
private object SAMPLE {

    //region сложный вариант навигации с аргументами
    enum class Sc2Args {
        INDEX
    }

    enum class Sc3Args {
        ID,
        NAME
    }

    sealed class TestRoute<T>(route: String? = null, navArgs: Map<T, NavType<*>> = mapOf()) : Route<T>(route, navArgs) {
        object TestScreen1 : TestRoute<Unit>()

        object TestScreen2 : TestRoute<Sc2Args>(
            navArgs = mapOf(
                Sc2Args.INDEX to NavType.IntType
            )
        ) {
            //SAMPLE: можно перегрузить функцию навигации для более удобного использования в navController
            fun navigate(index: Int) = navigate {
                when (it) {
                    Sc2Args.INDEX -> index
                }
            }
        }

        object TestScreen3 : TestRoute<Sc3Args>(
            navArgs = mapOf(
                Sc3Args.ID to NavType.IntType,
                Sc3Args.NAME to NavType.StringType,
            )
        )
    }
    //endregion

    //region упрощенный вариант навигации с аргументами

    enum class Sc4Args {
        ID,
        NAME,
        STATUS
    }

    object TestScreen4 : Route<Sc4Args>(
        navArgs = mapOf(
            Sc4Args.ID to NavType.IntType,
            Sc4Args.NAME to NavType.StringType,
            Sc4Args.STATUS to NavType.StringType
        )
    )

    //endregion

    //region вариант навигации к одному окну без аргументов

    object TestScreen5:Route<Unit>()

    //endregion
}
//endregion

