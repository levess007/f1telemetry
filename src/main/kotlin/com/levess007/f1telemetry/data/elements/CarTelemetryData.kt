package com.levess007.f1telemetry.data.elements

class CarTelemetryData {
    var speed = 0
    var throttle = 0
    var steer = 0
    var brake = 0
    var clutch = 0
    var gear = 0
    var engineRpm = 0
    var isDrs = false
    var revLightsPercent = 0
    var brakeTemperature: WheelData<Int?>? = null
    var tireSurfaceTemperature: WheelData<Int?>? = null
    var tireInnerTemperature: WheelData<Int?>? = null
    var engineTemperature = 0
    var tirePressure: WheelData<Float?>? = null
}