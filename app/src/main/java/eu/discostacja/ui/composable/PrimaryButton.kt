package eu.discostacja.ui.composable

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.discostacja.utils.noRippleClickable

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    SkewedBackgroundBox(
        modifier = modifier.noRippleClickable { onClick() },
        height = 20.dp
    ) {
        Text(
            text = text.uppercase(),
            fontSize = 10.sp,
            color = Color.White,
        )
    }
}