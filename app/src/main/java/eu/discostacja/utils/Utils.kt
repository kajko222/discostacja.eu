package eu.discostacja.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle


inline fun Modifier.noRippleClickable(
    crossinline onClick: () -> Unit
): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

val TextStyleWithoutPadding = TextStyle(
    platformStyle = PlatformTextStyle(
        includeFontPadding = false
    ),
)