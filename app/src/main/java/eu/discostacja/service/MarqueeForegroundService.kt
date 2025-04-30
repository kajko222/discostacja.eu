package eu.discostacja.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import eu.discostacja.AppPreferences
import eu.discostacja.MainActivity
import eu.discostacja.R
import eu.discostacja.model.MarqueeInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.inject
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

class MarqueeForegroundService : Service(), KoinComponent {

    companion object {
        const val ACTION_STOP = "ACTION_STOP_MONITORING"
    }

    private val channelId = "marquee_service_channel"
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var staticNotifyBuilder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager
    private lateinit var openAppIntent: PendingIntent
    private val marqueeFetcher: MarqueeFetcher by inject(MarqueeFetcher::class.java)

    override fun onCreate() {
        super.onCreate()
        initOpenIntent()
        startForegroundNotification()
        startFetchingLoop()
    }

    private fun startForegroundNotification() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Marquee Radio Monitor",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        val stopIntent = PendingIntent.getService(
            this,
            1,
            Intent(this, MarqueeForegroundService::class.java).apply {
                action = ACTION_STOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        staticNotifyBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("DiscoStacja - Radio Online")
            .setContentText("Rozwiń aby zobaczyć aktualną audycję!")
            .setSubText("Monitorowanie")
            .setSmallIcon(R.drawable.logo)
            .setContentIntent(openAppIntent)
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "ZAKOŃCZ",
                stopIntent
            )
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        startForeground(1, staticNotifyBuilder.build())
    }

    private fun startFetchingLoop() {
        serviceScope.launch {
            while (isActive) {
                try {
                    val oldMarqueeInfoMap: Map<String, MarqueeInfo?> = AppPreferences.marqueeInfo.value
                    val tempMarqueeInfoMap: MutableMap<String, MarqueeInfo?> = mutableMapOf()

                    var shouldShowNewAuditionNotification = false
                    var notificationText = ""
                    AppPreferences.radioStations.value.forEach { station ->
                        val oldMarqueeInfo = oldMarqueeInfoMap[station.name]
                        val newMarqueeInfo = marqueeFetcher.fetchMarqueeInfo(station)

                        val presenterChanged = oldMarqueeInfo?.presenter != null && oldMarqueeInfo.presenter != newMarqueeInfo?.presenter
                        val auditionChanged = oldMarqueeInfo?.audition != null && oldMarqueeInfo.audition != newMarqueeInfo?.audition

                        shouldShowNewAuditionNotification = presenterChanged || auditionChanged

                        if (newMarqueeInfo != null) {
                            tempMarqueeInfoMap[station.name] = newMarqueeInfo
                        }

                        notificationText += "📻 ${station.name} - ${newMarqueeInfo?.presenter}\n"
                    }

                    if (shouldShowNewAuditionNotification) sendSoundNotification()

                    // Update AppPreferencess
                    AppPreferences.marqueeInfo.update(tempMarqueeInfoMap)

                    // Update static notification
                    staticNotifyBuilder.setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(notificationText)
                    )
                    notificationManager.notify(1, staticNotifyBuilder.build())

                } catch (e: Exception) {
                    e.printStackTrace()
                }

                Log.d("DiscoStacja", "Pobieranie nowych danych z servera w tle...")
                delay(TimeUnit.MINUTES.toMillis(5))
            }
        }
    }

    private fun sendSoundNotification() {
        val notificationId = System.currentTimeMillis().toInt()
        val pushChannelId = "radio_updates_channel"

        if (notificationManager.getNotificationChannel(pushChannelId) == null) {
            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val channel = NotificationChannel(
                pushChannelId,
                "Nowe Audycje",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Powiadomienia o zmianie audycji"
                enableLights(true)
                enableVibration(true)
                setSound(soundUri, Notification.AUDIO_ATTRIBUTES_DEFAULT)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val soundNotification = NotificationCompat.Builder(this, pushChannelId)
            .setContentTitle("DiscoStacja - Radio Online")
            .setContentText("!!! Nowa audycja już jest !!!")
            .setSmallIcon(R.drawable.logo)
            .setContentIntent(openAppIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_SOUND)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, soundNotification)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    private fun killApp() {
        android.os.Process.killProcess(android.os.Process.myPid())
        exitProcess(0)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun initOpenIntent() {
        openAppIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopSelf()
            killApp()
            return START_NOT_STICKY
        }
        return START_STICKY
    }
}