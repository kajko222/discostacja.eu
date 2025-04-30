package eu.discostacja.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.discostacja.model.RadioStation
import eu.discostacja.player.PlayerState
import eu.discostacja.player.RadioPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val radioPlayer: RadioPlayer) : ViewModel() {

    private val _lastTriedStation = MutableStateFlow<RadioStation?>(null)
    val lastTriedStation = _lastTriedStation.asStateFlow()

    private val _selectedStation = MutableStateFlow<RadioStation?>(null)
    val selectedStation = _selectedStation.asStateFlow()

    val playerState: StateFlow<PlayerState> = radioPlayer.playerState

    fun togglePlayback(station: RadioStation) {
        viewModelScope.launch {
            _lastTriedStation.value = station

            if (playerState.value == PlayerState.Playing && _selectedStation.value == station) {
                radioPlayer.pause()
            } else {
                radioPlayer.stop()
                _selectedStation.value = station
                radioPlayer.playFromPls(station.plsUrl)
                if (playerState.value is PlayerState.Error) {
                    _selectedStation.value = null
                }
            }
        }
    }
}
