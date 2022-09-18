package ru.esstu.android.domain.modules.image_viewer.navigation

import androidx.navigation.NavType
import ru.esstu.android.domain.navigation.Route
import java.net.URLEncoder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

enum class ImageScreenArguments {
    START_IMAGE,
    IMAGES
}

object ImageScreen : Route<ImageScreenArguments>(
    navArgs = mapOf(
        ImageScreenArguments.START_IMAGE to NavType.StringType,
        ImageScreenArguments.IMAGES to NavType.StringType
    )
) {
    fun navigate(startImage: String, images: List<String>) = ImageScreen.navigate {
        when (it) {
            ImageScreenArguments.START_IMAGE -> startImage.encodeUri()
            ImageScreenArguments.IMAGES -> images.map { imageUri-> imageUri.encodeUri() }
        }
    }
}

fun String.encodeUri(charset: Charset = StandardCharsets.UTF_8): String = URLEncoder.encode(this, charset.toString())
