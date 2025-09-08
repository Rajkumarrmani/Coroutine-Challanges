package com.app.learningcoroutine.queue

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class QueueViewModel : ViewModel() {

    private val _orderChannel = Channel<Int>(25)
    val orderChannel = _orderChannel.receiveAsFlow()

    fun start() {
        val orders = (1..50).toList()
        viewModelScope.launch {
            for (x in orders) {
                _orderChannel.send(x)
                delay(250L)
            }
            _orderChannel.close()
        }
    }
}