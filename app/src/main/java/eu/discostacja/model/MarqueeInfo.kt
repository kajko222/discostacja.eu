package eu.discostacja.model

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.michaelflisar.kotpreferences.core.SettingsConverter
import kotlinx.parcelize.Parcelize
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.inject

@Parcelize
data class MarqueeInfo(
    val presenter: String = "",
    val audition: String = "",
    val rds: String = "",
    val avatarUrl: String = ""
) : Parcelable, KoinComponent {

    object CONVERTER : SettingsConverter<Map<String, MarqueeInfo?>, String> {
        private val gson by inject<Gson>(Gson::class.java)
        override fun from(data: String): Map<String, MarqueeInfo?> = gson.fromJson(data, object : TypeToken<Map<String, MarqueeInfo?>>() {}.type)
        override fun to(data: Map<String, MarqueeInfo?>): String = gson.toJson(data)
    }
}