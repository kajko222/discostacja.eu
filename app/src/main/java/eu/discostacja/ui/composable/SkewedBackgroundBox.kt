package eu.discostacja.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun SkewedBackgroundBox(
    modifier: Modifier = Modifier,
    height: Dp = 24.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .height(height)
            .clip(SkewedShape())
            .background(backgroundColor),
        contentAlignment = contentAlignment,
        content = content
    )
}

class SkewedShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val skew = size.height / 3
        val path = Path().apply {
            moveTo(skew, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width - skew, size.height)
            lineTo(0f, size.height)
            close()
        }
        return Outline.Generic(path)
    }
}