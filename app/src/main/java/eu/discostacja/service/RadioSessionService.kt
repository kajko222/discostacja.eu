package eu.discostacja.service

import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import eu.discostacja.player.RadioPlayer
import org.koin.mp.KoinPlatform.getKoin

class RadioSessionService : MediaSessionService() {

    private lateinit var mediaSession: MediaSession

    override fun onCreate() {
        super.onCreate()

        val radioPlayer: RadioPlayer = getKoin().get()
        val player = radioPlayer.getOrCreatePlayer()
        mediaSession = MediaSession.Builder(this, player)
            .setId("radio_session")
            .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession.release()
        super.onDestroy()
    }
}
