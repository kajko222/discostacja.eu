package eu.discostacja


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.navgraphs.RootNavGraph
import eu.discostacja.service.RadioSessionService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, RadioSessionService::class.java)
        startService(intent)

        setContent {
            DestinationsNavHost(
                navGraph = RootNavGraph
            )
        }
    }
}