package com.app.learningcoroutine.heartbeat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

val initialData = listOf(
    Stations(
        name = StationName.STATION_A,
        mode = Mode.ONLINE,
        lastSeen = 0.0
    ),
    Stations(
        name = StationName.STATION_B,
        mode = Mode.ONLINE,
        lastSeen = 0.0
    ),
    Stations(
        name = StationName.STATION_C,
        mode = Mode.ONLINE,
        lastSeen = 0.0
    )
)

class HeartBeatViewModel(

) : ViewModel() {

    private val _uiState = MutableStateFlow(initialData)
    val uiState = _uiState.asStateFlow()

    fun startHeatBeat() {
        viewModelScope.launch {
            while(true){
                delay(300L)
                println("Station 1 finished")
                delay(700L)
                println("Station 2 finished")
                delay(1000L)
                println("Station 3 finished")
            }
        }
    }
}