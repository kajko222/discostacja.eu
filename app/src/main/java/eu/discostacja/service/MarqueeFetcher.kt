package eu.discostacja.service

import android.content.Context
import android.util.Log
import eu.discostacja.AppPreferences
import eu.discostacja.model.MarqueeInfo
import eu.discostacja.model.RadioStation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class MarqueeFetcher(private val context: Context) {

    suspend fun fetchMarqueeInfo(station: RadioStation): MarqueeInfo? = withContext(Dispatchers.IO) {
        try {
            val marquesDoc = Jsoup.connect(station.marqueeUrl).get()
            val marquees = marquesDoc.select("marquee").map { it.text() }

            val avatarDoc = Jsoup.connect(station.jsoupAvatarUrl).get()
            val avatarUrl = avatarDoc.select("img").map { it }.first().absUrl("src")

            if (marquees.size >= 3) {
                MarqueeInfo(
                    presenter = marquees[0],
                    audition = marquees[1],
                    rds = marquees[2],
                    avatarUrl = avatarUrl
                )
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun updateMarquee() {
        try {
            val tempMarqueeInfoMap: MutableMap<String, MarqueeInfo?> = mutableMapOf()
            AppPreferences.radioStations.value.forEach { station ->
                val newMarqueeInfo = fetchMarqueeInfo(station)
                if (newMarqueeInfo != null) tempMarqueeInfoMap[station.name] = newMarqueeInfo
            }
            AppPreferences.marqueeInfo.update(tempMarqueeInfoMap)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Log.d("DiscoStacja", "Pobieranie nowych danych z servera app foreground...")
    }
}