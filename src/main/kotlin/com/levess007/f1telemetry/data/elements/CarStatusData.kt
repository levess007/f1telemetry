package com.levess007.f1telemetry.data.elements

class CarStatusData {
    var tractionControl = 0
    var isAntiLockBrakes = false
    var fuelMix = 0
    var frontBrakeBias = 0
    var isPitLimiterOn = false
    var fuelInTank = 0f
    var fuelCapacity = 0f
    var maxRpm = 0
    var idleRpm = 0
    var maxGears = 0
    var drsAllowed = 0
    var tiresWear: WheelData<Int?>? = null
    var tireCompound = 0
    var tiresDamage: WheelData<Int?>? = null
    var frontLeftWheelDamage = 0
    var frontRightWingDamage = 0
    var rearWingDamage = 0
    var engineDamage = 0
    var gearBoxDamage = 0
    var exhaustDamage = 0
    var vehicleFiaFlags = 0
    var ersStoreEngery = 0f
    var ersDeployMode = 0
    var ersHarvestedThisLapMGUK = 0f
    var ersHarvestedThisLapMGUH = 0f
    var ersDeployedThisLap = 0f
}