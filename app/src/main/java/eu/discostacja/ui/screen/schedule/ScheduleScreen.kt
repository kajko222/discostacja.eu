package eu.discostacja.ui.screen.schedule

import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import eu.discostacja.MainGraph
import eu.discostacja.model.RadioStation

object DismissableDialog : DestinationStyle.Dialog() {
    override val properties = DialogProperties(
        dismissOnClickOutside = true,
        dismissOnBackPress = true,
    )
}

@Destination<MainGraph>(style = DismissableDialog::class)
@Composable
fun ScheduleScreen(
    navigator: DestinationsNavigator,
    radioStation: RadioStation
) {
    Dialog(onDismissRequest = { navigator.navigateUp() }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White
        ) {
            AndroidView(factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
            }, update = {
                it.loadUrl(radioStation.scheduleUrl)
            })
        }
    }
}