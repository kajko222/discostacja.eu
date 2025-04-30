package eu.discostacja.model

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.michaelflisar.kotpreferences.core.SettingsConverter
import kotlinx.parcelize.Parcelize
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.inject

@Parcelize
data class RadioStation(
    val name: String,
    val plsUrl: String,
    val marqueeUrl: String,
    val jsoupAvatarUrl: String,
    val regardsFormUrl: String,
    val scheduleUrl: String
) : Parcelable, KoinComponent {

    object CONVERTER : SettingsConverter<List<RadioStation>, String> {
        private val gson by inject<Gson>(Gson::class.java)
        override fun from(data: String): List<RadioStation> = gson.fromJson(data, object : TypeToken<List<RadioStation>>() {}.type)
        override fun to(data: List<RadioStation>): String = gson.toJson(data)
    }
}