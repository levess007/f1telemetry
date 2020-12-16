package com.levess007.f1telemetry.data.elements

data class LapData(
    val carIndex: Float,
    val resultStatus: ResultStatus? = null,
    val isPlayersCar: Boolean,
    val lastLapTime: Float,
    val currentLapTime: Float,
    val bestLaptTime: Float,
    val sector1Time: Float,
    val sector2Time: Float,
    val lapDistance: Float,
    val totalDistance: Float,
    val safetyCarDelta: Float,
    val carPosition: Int,
    val currentLapNum: Int,
    val pitStatus: PitStatus? = null,
    val sector: Int,
    val isCurrentLapInvalid: Boolean,
    val penalties: Int,
    val gridPosition: Int,
    val driverStatus: DriverStatus? = null
)