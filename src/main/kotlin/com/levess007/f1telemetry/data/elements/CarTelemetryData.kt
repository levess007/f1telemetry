package com.levess007.f1telemetry.data.elements

data class CarTelemetryData(
    val speed: Int,
    val throttle: Int,
    val steer: Int,
    val brake: Int,
    val clutch: Int,
    val gear: Int,
    val engineRpm: Int,
    val isDrs: Boolean,
    val revLightsPercent: Int,
    val brakeTemperature: WheelData<Int?>?,
    val tireSurfaceTemperature: WheelData<Int?>?,
    val tireInnerTemperature: WheelData<Int?>?,
    val engineTemperature: Int,
    val tirePressure: WheelData<Float?>? = null
)