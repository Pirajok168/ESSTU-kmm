package ru.esstu.android.authorized.messaging.dialog_chat.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.esstu.android.domain.ui.theme.CompPreviewTheme
import kotlin.math.abs

@Composable
fun SwipeableCard(distance: Dp = 80.dp, onDragged: () -> Unit, backLayerContent: @Composable (percentage: Float) -> Unit, frontLayerContent: @Composable () -> Unit) {
    val scope = rememberCoroutineScope()

    var isDragActive by remember { mutableStateOf(false) }
    val (minPx, maxPx) = with(LocalDensity.current) { -distance.toPx() to 0.dp.toPx() }
    val offset = remember { Animatable(0f) }

    Box(contentAlignment = Alignment.CenterEnd) {
        Box(
            modifier = Modifier
                .offset { IntOffset(offset.value.toInt(), 0) }
                .draggable(
                    state = rememberDraggableState {
                        scope.launch {
                            offset.snapTo((offset.value + it).coerceIn(minPx, maxPx))
                        }
                    },
                    orientation = Orientation.Horizontal,
                    onDragStarted = { isDragActive = true },
                    onDragStopped = {
                        if (isDragActive && offset.value <= (minPx * 0.9))
                            onDragged()

                        isDragActive = false
                        offset.animateTo(0f)
                    }
                ))
        {
            frontLayerContent()
        }
        Box(
            modifier = Modifier.width(with(LocalDensity.current) { abs(offset.value).toDp() }),
            contentAlignment = Alignment.CenterEnd
        ) {
            backLayerContent(offset.value / minPx)
        }
    }
}

@Preview
@Composable
fun scp() {
    CompPreviewTheme {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            var counter by remember {
                mutableStateOf(0)
            }
            SwipeableCard(
                backLayerContent = { progress->
                    Box(
                        Modifier
                            .alpha(progress)
                            .fillMaxWidth()
                            .height(80.dp)
                            .background(Color.Magenta)
                    )
                },
                frontLayerContent = {
                    Box(
                        Modifier
                            .border(2.dp, color = Color.Black)
                            .width(200.dp)
                            .height(80.dp)
                            .background(Color.Cyan)
                    )
                },
                onDragged = {
                    counter++
                })
            Text(text = counter.toString())
        }
    }
}