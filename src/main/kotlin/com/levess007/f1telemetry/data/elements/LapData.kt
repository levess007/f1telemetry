package com.levess007.f1telemetry.data.elements

class LapData {
    var carIndex = 0f
    var isPlayersCar = false
    var lastLapTime = 0f
    var currentLapTime = 0f
    var bestLaptTime = 0f
    var sector1Time = 0f
    var sector2Time = 0f
    var lapDistance = 0f
    var totalDistance = 0f
    var safetyCarDelta = 0f
    var carPosition = 0
    var currentLapNum = 0
    var pitStatus: PitStatus? = null
    var sector = 0
    var isCurrentLapInvalid = false
    var penalties = 0
    var gridPosition = 0
    var driverStatus: DriverStatus? = null
    var resultStatus: ResultStatus? = null
}