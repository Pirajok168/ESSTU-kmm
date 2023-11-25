package ru.esstu.android.authorized.messaging.dialog_chat.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import kotlin.math.max

@Composable
fun TimePlaceholder(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content,
        measurePolicy = { measurables, constraints ->

            if (measurables.size == 1) {
                val contentPlaceable = measurables.first().measure(constraints)

                return@Layout layout(contentPlaceable.measuredWidth, contentPlaceable.measuredHeight) {
                    contentPlaceable.placeRelative(0, 0)
                }
            }

            val contentPlaceable = measurables.first().measure(constraints)
            val timePlaceable = measurables.last().measure(constraints)

            if (contentPlaceable.measuredWidth + timePlaceable.measuredWidth <= constraints.maxWidth) {

                val contentWidth = contentPlaceable.measuredWidth + timePlaceable.measuredWidth
                val contentHeight = max(contentPlaceable.measuredHeight, timePlaceable.measuredHeight)

                layout(contentWidth, contentHeight) {
                    contentPlaceable.placeRelative(0, contentHeight - contentPlaceable.measuredHeight)
                    timePlaceable.placeRelative(contentPlaceable.measuredWidth, contentHeight - timePlaceable.measuredHeight)
                }

            } else {

                val contentWidth = max(timePlaceable.measuredWidth, contentPlaceable.measuredWidth)
                val contentHeight = contentPlaceable.measuredHeight + timePlaceable.measuredHeight

                layout(contentWidth, contentHeight) {
                    contentPlaceable.placeRelative(0, 0)
                    timePlaceable.placeRelative(contentWidth - timePlaceable.measuredWidth, contentPlaceable.measuredHeight)
                }

            }
        }
    )
}