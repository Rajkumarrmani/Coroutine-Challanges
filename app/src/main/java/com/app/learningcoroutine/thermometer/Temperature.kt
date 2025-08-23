package com.app.learningcoroutine.thermometer

data class TempData(
    val totalCount: Int,
    val data: List<Temperature> = list
)

data class Temperature(
    val index: Int,
    val isEnabled: Boolean = false,
    val tempValue: String = "",
)

val list = listOf(
    Temperature(1),
    Temperature(2),
    Temperature(3),
    Temperature(4),
    Temperature(5),
    Temperature(6),
    Temperature(7),
    Temperature(8),
    Temperature(9),
    Temperature(10),
    Temperature(11),
    Temperature(12),
    Temperature(13),
    Temperature(14),
    Temperature(15),
    Temperature(16),
    Temperature(17),
    Temperature(18),
    Temperature(19),
    Temperature(20)
)
