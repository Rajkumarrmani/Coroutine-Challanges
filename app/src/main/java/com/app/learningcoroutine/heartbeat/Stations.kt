package com.app.learningcoroutine.heartbeat

import android.R.attr.mode
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class Stations(
    var name: StationName,
    var mode: Mode,
    var lastSeen: Double,
)

enum class StationName {
    STATION_A {
        override fun toString(): String = "STATION A"
    },
    STATION_B {
        override fun toString(): String = "STATION B"
    },
    STATION_C {
        override fun toString(): String = "STATION C"
    }
}

enum class Mode {
    ONLINE,
    OFFLINE
}


fun main() {


}