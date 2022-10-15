package ru.esstu.android.student.messaging.dialog_chat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import ru.esstu.android.domain.ui.theme.CompPreviewTheme
import kotlin.random.Random

@Suppress("UnnecessaryVariable")
@Composable
fun ImageGrid(rows: Int = 2, content: @Composable () -> Unit) {
    Layout(content = content, measurePolicy = { measurables, constraints ->
        val placeableWidth = constraints.maxWidth / rows
        val placeableHeight = placeableWidth

        val extraMeasurablesSize = (measurables.size % rows)

        val gridMeasurables = measurables.take(measurables.size - extraMeasurablesSize)
        val extraMeasurables = measurables.takeLast(extraMeasurablesSize)

        val gridPlaceables = gridMeasurables.map {
            it.measure(constraints.copy(maxWidth = placeableWidth, maxHeight = placeableHeight))
        }

        val extraPlaceables = extraMeasurables.map {
            it.measure(constraints.copy(maxHeight = constraints.maxWidth))
        }

        val rowsCount = gridPlaceables.size / rows
        val layoutHeight = placeableHeight * rowsCount + extraPlaceables.sumOf { it.height }

        layout(height = layoutHeight, width = constraints.maxWidth) {
            gridPlaceables.forEachIndexed { index, placeable ->
                val x = index % rows
                val y = index / rows
                placeable.placeRelative(x = x * placeableWidth, y = y * placeableHeight)
            }
            extraPlaceables.forEachIndexed { index, placeable ->
                val extraHeight = constraints.maxWidth
                placeable.placeRelative(0, rowsCount * placeableHeight + extraHeight * index)
            }
        }
    })
}


@Suppress("UnnecessaryVariable")
@Composable
fun ImageGridV2(rows: Int = 2, content: @Composable () -> Unit) {
    Layout(content = content, measurePolicy = { measurables, constraints ->
        val placeableWidth = constraints.maxWidth / rows
        val placeableHeight = placeableWidth

        val extraMeasurablesCount = (measurables.size % rows)

        val gridMeasurables = measurables.take(measurables.size - extraMeasurablesCount)
        val extraMeasurables = measurables.takeLast(extraMeasurablesCount)

        val gridPlaceables = gridMeasurables.map {
            it.measure(constraints.copy(maxWidth = placeableWidth, maxHeight = placeableHeight))
        }

        val extraPlaceablesCount = extraMeasurables.size
        val extraPlaceablesSize = if (extraPlaceablesCount > 0) constraints.maxWidth / extraPlaceablesCount else 0
        val extraPlaceables = extraMeasurables.map {
            it.measure(constraints.copy(maxHeight = extraPlaceablesSize, maxWidth = extraPlaceablesSize))
        }

        val rowsCount = gridPlaceables.size / rows
        val layoutHeight = placeableHeight * rowsCount + extraPlaceablesSize

        layout(height = layoutHeight, width = constraints.maxWidth) {
            gridPlaceables.forEachIndexed { index, placeable ->
                val x = index % rows
                val y = index / rows
                placeable.placeRelative(x = x * placeableWidth, y = y * placeableHeight)
            }
            extraPlaceables.forEachIndexed { index, placeable ->
                val extraHeight = constraints.maxWidth
                placeable.placeRelative(extraPlaceablesSize * index, rowsCount * placeableHeight)
            }
        }
    })
}

@Preview()
@Composable
fun igp() {
    CompPreviewTheme {
        ImageGridV2(3) {
            (0..4).forEach { _ ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Color
                                (
                                Random.nextInt(0, 255),
                                Random.nextInt(0, 255),
                                Random.nextInt(0, 255)
                            )
                        )
                )
            }
        }
    }
}