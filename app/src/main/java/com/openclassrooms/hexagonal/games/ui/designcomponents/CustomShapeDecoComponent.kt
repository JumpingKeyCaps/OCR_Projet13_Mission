package com.openclassrooms.hexagonal.games.ui.designcomponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.openclassrooms.hexagonal.games.ui.theme.Purple40

/**
 * Custom decoration shape component for the top of the screen.
 * @param height The height of the component.
 * @param oscillations The number of oscillations in the wave.
 * @param waveHeight The height of the wave.
 */
@Composable
fun CustomShapeComponent(height: Float , oscillations: Int = 1, waveHeight: Float = 0.2f) {
    Surface(
        modifier = Modifier.fillMaxWidth().height(height.dp),
        color = Purple40,
        shape = BottomWaveShape(oscillations,waveHeight)  // Apply custom shape
    ) {}
}


/**
 * Custom shape builder class
 * @param oscillations The number of oscillations in the wave.
 * @param waveHeight The height of the wave.
 * @return A custom shape.
 */
class BottomWaveShape(private val oscillations: Int = 1, private val waveHeight: Float = 0.2f) :
    Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
        density: Density
    ) = Outline.Generic(
        path = Path().apply {
            // Start at the top-left corner
            moveTo(0f, 0f)

            // Draw the flat top and sides
            lineTo(0f, size.height * 1f)  // Adjust to control where the wave starts
            val waveHeight = size.height * waveHeight  // Adjust wave height
            val waveWidth = size.width / (oscillations * 2)  // Width per oscillation

            // Draw the wave pattern along the bottom
            for (i in 0 until oscillations) {
                val startX = i * 2 * waveWidth
                quadraticBezierTo(
                    startX + waveWidth / 2, size.height + waveHeight,
                    startX + waveWidth, size.height
                )
                quadraticBezierTo(
                    startX + 1.5f * waveWidth, size.height - waveHeight,
                    startX + 2 * waveWidth, size.height
                )
            }
            // Close the path to form the shape
            lineTo(size.width, 0f)
            close()
        }
    )
}
