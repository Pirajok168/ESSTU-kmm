package ru.esstu.android.student.news.announcement_screen.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.Layout

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