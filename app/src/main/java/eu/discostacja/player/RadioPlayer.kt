package eu.discostacja.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.net.URL

sealed class PlayerState {
    data object Idle : PlayerState()
    data object Loading : PlayerState()
    data object Playing : PlayerState()
    data object Paused : PlayerState()
    data class Error(val message: String) : PlayerState()
}

class RadioPlayer(private val context: Context) {
    private var exoPlayer: ExoPlayer? = null
    val playerState = MutableStateFlow<PlayerState>(PlayerState.Idle)
    var onMetadataUpdated: ((MediaMetadata) -> Unit)? = null

    suspend fun playFromPls(url: String) {
        playerState.value = PlayerState.Loading
        val streamUrl = parsePlsFile(url)
        if (streamUrl != null) {
            if (exoPlayer == null) {
                exoPlayer = ExoPlayer.Builder(context).build()
            } else {
                exoPlayer?.release()
                exoPlayer = ExoPlayer.Builder(context).build()
            }

            exoPlayer?.apply {
                setMediaItem(MediaItem.fromUri(streamUrl))
                playWhenReady = true
                prepare()
            }

            exoPlayer?.addListener(object : Player.Listener {
                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                    onMetadataUpdated?.invoke(mediaMetadata)
                }
            })

            playerState.value = PlayerState.Playing
        } else {
            playerState.value = PlayerState.Error("Nie udało się odczytać pliku .pls")
        }
    }

    fun getOrCreatePlayer(): ExoPlayer {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build()
        }
        return exoPlayer!!
    }

    fun pause() {
        exoPlayer?.pause()
        playerState.value = PlayerState.Paused
    }

    fun stop() {
        exoPlayer?.release()
        exoPlayer = null
        playerState.value = PlayerState.Idle
    }

    private suspend fun parsePlsFile(url: String): String? = withContext(Dispatchers.IO) {
        try {
            val connection = URL(url).openStream().bufferedReader()
            connection.useLines { lines ->
                lines.firstOrNull { it.startsWith("File1=") }?.substringAfter("File1=")
            }
        } catch (e: Exception) {
            null
        }
    }
}