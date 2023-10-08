package ru.esstu.android.domain.modules.image_viewer.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageScreen(
    imageUris: List<String>,
    startImage: String = "",
    minScale: Float = 1f,
    maxScale: Float = 4f,
    onBackPress: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.TopStart
    ) {
        val scope = rememberCoroutineScope()
        val pagerState = rememberPagerState {
            imageUris.size
        }

        val systemUiController = rememberSystemUiController()

        val isLightPaletteSelected = MaterialTheme.colors.isLight
        DisposableEffect(Unit) {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = false
            )
            onDispose {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = isLightPaletteSelected
                )
            }
        }

        LaunchedEffect(key1 = Unit, block = {
            val startIndex = imageUris.indexOf(startImage)
            if (startIndex >= 0) pagerState.scrollToPage(startIndex)
        })

        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.CenterVertically
        ) { index ->
            val defaultScale = 1f.coerceIn(minScale, maxScale)

            val scale = remember { Animatable(defaultScale) }
            val state = rememberTransformableState { zoomChange, _, _ ->
                scope.launch {
                    val updatedScale = scale.value * zoomChange
                    scale.snapTo(updatedScale.coerceIn(minScale, maxScale))
                }
            }

            val isScrolling = pagerState.targetPage != pagerState.currentPage
            LaunchedEffect(key1 = isScrolling, block = {
                scale.snapTo(defaultScale)
            })

            GlideImage(
                modifier = Modifier
                    .graphicsLayer(
                        scaleX = scale.value,
                        scaleY = scale.value
                    )
                    .transformable(state)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                scope.launch {
                                    if (scale.value > minScale)
                                        scale.animateTo(minScale)
                                    else
                                        scale.animateTo(maxScale)
                                }
                            }
                        )
                    },
                imageModel = imageUris[index],
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center,
                loading = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        CircularProgressIndicator(color = Color.White)
                    }
                }
            )
        }

        IconButton(
            modifier = Modifier.statusBarsPadding(),
            onClick = onBackPress
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}
