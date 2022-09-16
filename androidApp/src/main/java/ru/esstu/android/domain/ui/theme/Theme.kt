package ru.esstu.android.domain.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = SeaGreen,
    primaryVariant = SeaGreen,
    secondary = Teal200,
    onPrimary = Color.White,
    secondaryVariant = Orange,
    onSecondary = Color.White
)

private val LightColorPalette = lightColors(
    primary = SeaGreen,
    primaryVariant = SeaGreen,
    onPrimary = Color.White,
    secondary = Orange,
    secondaryVariant = Orange,
    onSecondary = Color.White,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)
val Colors.tertiary: Color
    get() = LightGray
val Colors.onTertiary: Color
    get() = Color.White

@Composable
fun EsstuTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = {
            ProvideWindowInsets {
                val systemUiController = rememberSystemUiController()

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = !darkTheme
                    )
                }

                content()
            }
        }
    )
}

@Composable
fun CompPreviewTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}