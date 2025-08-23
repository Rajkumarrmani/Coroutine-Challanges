package com.app.learningcoroutine.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Destination : NavKey {
    @Serializable
    data object Landing : Destination
    @Serializable
    data object Home : Destination
}