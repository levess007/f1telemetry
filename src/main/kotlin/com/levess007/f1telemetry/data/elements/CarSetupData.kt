package com.levess007.f1telemetry.data.elements

data class CarSetupData(
    val frontWing: Int = 0,
    val rearWing: Int = 0,
    val onThrottle: Int = 0,
    val offThrottle: Int = 0,
    val frontCamber: Float = 0f,
    val rearCamber: Float = 0f,
    val frontToe: Float = 0f,
    val rearToe: Float = 0f,
    val frontSuspension: Int = 0,
    val rearSuspension: Int = 0,
    val frontAntiRollBar: Int = 0,
    val rearAntiRollBar: Int = 0,
    val frontSuspensionHeight: Int = 0,
    val rearSuspensionHeight: Int = 0,
    val brakePressure: Int = 0,
    val brakeBias: Int = 0,
    val frontTirePressure: Float = 0f,
    val rearTirePressure: Float = 0f,
    val ballast: Int = 0,
    val fuelLoad: Float = 0f,
)