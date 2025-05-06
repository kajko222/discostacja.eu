package eu.discostacja


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.navgraphs.RootNavGraph
import eu.discostacja.service.RadioSessionService
import eu.discostacja.utils.isAppExpired
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isAppExpired()) {
            finishAffinity()
            exitProcess(0)
        }

        WindowCompat.setDecorFitsSystemWindows(window, true)

        val intent = Intent(this, RadioSessionService::class.java)
        startService(intent)

        setContent {
            DestinationsNavHost(
                navGraph = RootNavGraph
            )
        }
    }
}