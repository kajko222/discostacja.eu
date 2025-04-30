package eu.discostacja

import com.michaelflisar.kotpreferences.core.SettingsModel
import com.michaelflisar.kotpreferences.storage.datastore.DataStoreStorage
import com.michaelflisar.kotpreferences.storage.datastore.create
import eu.discostacja.model.MarqueeInfo
import eu.discostacja.model.RadioStation

object AppPreferences : SettingsModel(DataStoreStorage.create(name = "disco_stacja_preferences")) {

    val enableNotifications by boolPref(true)

    val marqueeInfo by anyStringPref(
        MarqueeInfo.CONVERTER,
        emptyMap()
    )

    val radioStations by anyStringPref(
        RadioStation.CONVERTER,
        listOf(
            RadioStation(
                name = "Kanał PartyMix",
                plsUrl = "https://s3.slotex.pl/shoutcast/7442/listen.pls",
                marqueeUrl = "https://discostacja.panelradiowy.pl/styl11/staty2.php?ip=aS9wOHdiMnRROGRKTHRqWDBwOE5TUT09&port=b1hCVWNaK3RkNnVnemRQeGUxdVZkUT09&v=2&type=1&style=6&time=60&color=ffffff&idp=79166&listeners=2#",
                jsoupAvatarUrl = "https://discostacja.panelradiowy.pl/embed.php?script=avatar&size=120",
                regardsFormUrl = "https://discostacja.panelradiowy.pl/embed.php?script=onlineform",
                scheduleUrl = "https://discostacja.panelradiowy.pl/embed.php?script=ramowka"
            ),
            RadioStation(
                name = "Kanał Disco",
                plsUrl = "https://s3.slotex.pl/shoutcast/7448/listen.pls",
                marqueeUrl = "https://danceparty.panelradiowy.pl/styl11/staty2.php?ip=aS9wOHdiMnRROGRKTHRqWDBwOE5TUT09&port=eEFYVVVXeEhnb0JReDFFOS9oNkpBZz09&v=2&type=1&style=1&time=60&color=000000&idp=79154&listeners=2",
                jsoupAvatarUrl = "https://danceparty.panelradiowy.pl/embed.php?script=avatar&size=120",
                regardsFormUrl = "https://danceparty.panelradiowy.pl/embed.php?script=onlineform",
                scheduleUrl = "https://danceparty.panelradiowy.pl/embed.php?script=ramowka"
            )
        )
    )
}