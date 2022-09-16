package ru.esstu.android.domain.navigation.bottom_navigation.util

import androidx.annotation.DrawableRes
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource

sealed class IconResource {
    data class DrawableResource(@DrawableRes val drawable: Int) : IconResource()
    data class VectorResource(val vector: ImageVector) : IconResource()

    @Composable
    fun AsIcon(tintColor: Color? = null) {
        if (tintColor == null)
            when (this) {
                is DrawableResource -> Icon(painter = painterResource(id = drawable), contentDescription = null)
                is VectorResource -> Icon(imageVector = vector, contentDescription = null)
            }
        else
            when (this) {
                is DrawableResource -> Icon(painter = painterResource(id = drawable), contentDescription = null, tint = tintColor)
                is VectorResource -> Icon(imageVector = vector, contentDescription = null, tint = tintColor)
            }
    }
}