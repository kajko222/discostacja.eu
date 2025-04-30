package eu.discostacja.ui.screen.main

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startForegroundService
import coil3.ImageLoader
import coil3.compose.rememberAsyncImagePainter
import com.michaelflisar.composepreferences.core.PreferenceScreen
import com.michaelflisar.composepreferences.kotpreferences.collectSetting
import com.michaelflisar.composepreferences.screen.bool.PreferenceBool
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.RegardsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ScheduleScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import eu.discostacja.AppPreferences
import eu.discostacja.MainGraph
import eu.discostacja.R
import eu.discostacja.player.PlayerState
import eu.discostacja.service.MarqueeFetcher
import eu.discostacja.service.MarqueeForegroundService
import eu.discostacja.ui.composable.PrimaryButton
import eu.discostacja.ui.composable.SkewedBackgroundBox
import eu.discostacja.ui.screen.main.composable.StationShimmer
import eu.discostacja.ui.screen.main.composable.TopBar
import eu.discostacja.utils.ObserveLifecycleEvents
import eu.discostacja.utils.TextStyleWithoutPadding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.inject

@Destination<MainGraph>(start = true)
@Composable
fun MainScreen(
    navigator: DestinationsNavigator,
    viewModel: MainViewModel = koinViewModel()
) {

    val imageLoader by inject<ImageLoader>(ImageLoader::class.java)
    val coroutineScope = rememberCoroutineScope()

    val lastTriedStation by viewModel.lastTriedStation.collectAsState()
    val selectedStation by viewModel.selectedStation.collectAsState()
    val playerState by viewModel.playerState.collectAsState()
    val marqueeInfoMap = AppPreferences.marqueeInfo.collectSetting().value

    val context = LocalContext.current
    val marqueeFetcher: MarqueeFetcher by inject(MarqueeFetcher::class.java)
    var shouldMonitoringForeground = true

    LaunchedEffect(Unit) {
        while (true) {
            if (!AppPreferences.enableNotifications.value && shouldMonitoringForeground) {
                marqueeFetcher.updateMarquee()
            }
            delay(15000)
        }
    }

    ObserveLifecycleEvents(
        onPause = {
            if (!AppPreferences.enableNotifications.value) {
                val serviceIntent = Intent(context, MarqueeForegroundService::class.java)
                context.stopService(serviceIntent)
            }
            shouldMonitoringForeground = false
        },
        onResume = {
            val serviceIntent = Intent(context, MarqueeForegroundService::class.java)
            if (AppPreferences.enableNotifications.value) {
                startForegroundService(context, serviceIntent)
            } else {
                context.stopService(serviceIntent)
            }
            shouldMonitoringForeground = true
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        TopBar()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            AppPreferences.radioStations.value.forEach { station ->

                val marqueeInfo = marqueeInfoMap[station.name]
                val isSelected = selectedStation == station
                val isLastTried = lastTriedStation == station

                Column {
                    if (marqueeInfo == null) {
                        StationShimmer()
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SkewedBackgroundBox(
                                modifier = Modifier.width(160.dp),
                                height = 20.dp,
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(horizontal = 12.dp)
                                        .padding(top = 2.dp)
                                        .fillMaxSize(),
                                    text = station.name.uppercase(),
                                    color = Color.White,
                                    style = TextStyleWithoutPadding,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(3.dp)
                                    .background(MaterialTheme.colorScheme.primary),
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = marqueeInfo.avatarUrl,
                                    imageLoader = imageLoader
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(6.dp)),
                            )
                            Column(
                                modifier = Modifier.weight(0.8f),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Text(
                                    text = marqueeInfo.presenter,
                                    fontSize = 13.sp,
                                )
                                Text(
                                    text = marqueeInfo.audition,
                                    fontSize = 13.sp,
                                )
                                Text(
                                    text = marqueeInfo.rds,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .basicMarquee()
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    PrimaryButton(
                                        modifier = Modifier.weight(1f),
                                        onClick = { navigator.navigate(RegardsScreenDestination(station)) },
                                        text = stringResource(R.string.main_screen_regards_button)
                                    )
                                    PrimaryButton(
                                        modifier = Modifier.weight(1f),
                                        onClick = { navigator.navigate(ScheduleScreenDestination(station)) },
                                        text = stringResource(R.string.main_screen_schedule_button)
                                    )
                                }
                            }
                            Column {
                                IconButton(onClick = { viewModel.togglePlayback(station) }) {

                                    when (playerState) {
                                        is PlayerState.Playing -> if (isSelected) Icon(Icons.Default.Pause, contentDescription = null) else Icon(
                                            Icons.Default.PlayArrow,
                                            contentDescription = null
                                        )

                                        is PlayerState.Paused -> Icon(Icons.Default.PlayArrow, contentDescription = null)
                                        is PlayerState.Loading -> if (isLastTried) Icon(Icons.Default.HourglassEmpty, null) else Icon(
                                            Icons.Default.PlayArrow, null
                                        )

                                        is PlayerState.Error -> if (isLastTried) Icon(Icons.Default.Error, null) else Icon(
                                            Icons.Default.PlayArrow, null
                                        )

                                        else -> Icon(Icons.Default.PlayArrow, contentDescription = null)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        PreferenceScreen(
            modifier = Modifier.padding(bottom = 12.dp),
            scrollable = false
        ) {
            PreferenceBool(
                value = AppPreferences.enableNotifications.collectSetting().value,
                onValueChange = {
                    coroutineScope.launch(Dispatchers.IO) {
                        AppPreferences.enableNotifications.update(it)
                    }

                    val serviceIntent = Intent(context, MarqueeForegroundService::class.java)
                    if (it) {
                        startForegroundService(context, serviceIntent)
                    } else {
                        context.stopService(serviceIntent)
                    }
                },
                title = stringResource(R.string.main_screen_regards_settings_title),
                subtitle = stringResource(R.string.main_screen_regards_settings_desc),
            )
        }
    }
}