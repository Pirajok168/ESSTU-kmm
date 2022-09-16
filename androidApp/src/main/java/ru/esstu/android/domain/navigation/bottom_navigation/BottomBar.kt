package ru.esstu.android.domain.navigation.bottom_navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.esstu.android.domain.navigation.bottom_navigation.util.NavItem
import ru.esstu.android.domain.navigation.Route

@Composable
fun BottomBar(modifier: Modifier = Modifier, navController: NavController, vararg tab: NavItem, onTabSelected: (Route<*>) -> Unit = {}) {

    var bottomNavColor by remember { mutableStateOf<Color?>(null) }

    BottomNavigation(modifier = modifier, backgroundColor = bottomNavColor ?: MaterialTheme.colors.background, elevation = 16.dp) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        tab.forEach { screen ->

            val isItemSelected = currentDestination?.hierarchy?.any { it.route == screen.route.startDest() } == true

            if (isItemSelected) {
                onTabSelected(screen.route)
                bottomNavColor = screen.color
            }
            CustomBottomNavItem(
                onClick = {
                    navController.navigate(screen.route.passRoute()) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                isItemSelected = isItemSelected,
                icon ={ screen.icon.AsIcon() },
                label = screen.label
            )


        }
    }
}

@Composable
fun RowScope.CustomBottomNavItem(
    onClick: () -> Unit,
    isItemSelected: Boolean,
    icon: @Composable () -> Unit,
    label: String,
    selectedContentColor: Color = LocalContentColor.current,
    unselectedContentColor: Color = selectedContentColor.copy(alpha = ContentAlpha.medium)
){
    val ripple = rememberRipple(bounded = false, color = selectedContentColor)
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .selectable(
                selected = isItemSelected,
                onClick = onClick,
                enabled = true,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = ripple
            )
            .weight(1f), contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            BottomNavigationTransition(
                selectedContentColor,
                unselectedContentColor,
                isItemSelected
            ){
                    progress ->

                icon()
                Text(
                    text = label,
                    overflow = TextOverflow.Visible,
                    fontWeight = FontWeight.SemiBold,
                    softWrap = false,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                )
            }

        }
    }
}

@Composable
private fun BottomNavigationTransition(
    activeColor: Color,
    inactiveColor: Color,
    selected: Boolean,
    content: @Composable (animationProgress: Float) -> Unit
) {
    val animationProgress by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = BottomNavigationAnimationSpec
    )

    val color = lerp(inactiveColor, activeColor, animationProgress)

    CompositionLocalProvider(
        LocalContentColor provides color.copy(alpha = 1f),
        LocalContentAlpha provides color.alpha,
    ) {
        content(animationProgress)
    }
}

private val BottomNavigationAnimationSpec = TweenSpec<Float>(
    durationMillis = 300,
    easing = FastOutSlowInEasing
)