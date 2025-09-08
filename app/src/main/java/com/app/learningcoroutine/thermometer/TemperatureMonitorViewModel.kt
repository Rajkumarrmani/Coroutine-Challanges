package com.app.learningcoroutine.thermometer

import android.util.Log
import android.util.Log.e
import android.util.Log.i
import androidx.compose.ui.input.key.Key.Companion.F
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.withIndex
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.util.stream.Collectors.toList

class TemperatureMonitorViewModel : ViewModel() {

    val mockData = listOf(
        -100.0,
        0.0,
        -30.5,
        10.0,
        20.0,
        -60.0,
        35.5,
        -51.3,
        22.0,
        19.8,
        45.0,
        55.1,
        60.2,
        90.0,
        -49.9,
        5.0,
        12.3,
        8.8,
        0.5,
        -2.0,
        30.0,
        35.0,
        27.5,
        18.1,
        15.6,
        11.0,
        17.3,
        33.8,
        41.2,
        -80.0
    )

    init {
        observeData()
    }

    private val _items = MutableStateFlow(TempData(0))
    val items: MutableStateFlow<TempData> = _items


    fun clear() {
        _items.update { currentList ->

            val updatedList = currentList.data.mapIndexed { i, temp ->
                temp.copy(
                    isEnabled = false,
                    tempValue = ""
                )
            }

            currentList.copy(
                totalCount = 0,
                data = updatedList
            )
        }

        observeData()
    }


    fun observeData() {
        viewModelScope.launch {
            startMonitoring().collect { (index, value) ->
                _items.update { currentList ->

                    val updatedList = currentList.data.mapIndexed { i, temp ->
                        if (i == index) {
                            temp.copy(
                                isEnabled = true,
                                tempValue = value
                            )
                        } else temp
                    }
                    currentList.copy(
                        totalCount = index + 1,
                        data = updatedList
                    )
                }
            }
        }
    }

    fun startMonitoring(): Flow<Pair<Int, String>> = flow {

        mockData.asFlow()
            .filter { it > -50.0 }
            .take(20)
            .onEach { delay(250) }
            .withIndex()
            .collect { (index, value) ->
                val fahrenheit = (value * 9 / 5) + 32
                val roundedFahrenheit = fahrenheit
                    .toBigDecimal()
                    .setScale(2, RoundingMode.HALF_UP)
                    .toDouble()
                emit(index to "$roundedFahrenheit °F")
            }
    }

}