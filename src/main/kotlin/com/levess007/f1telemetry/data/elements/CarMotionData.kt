package com.levess007.f1telemetry.data.elements

data class CarMotionData(
    val carIndex: Int,
    val isPlayersCar: Boolean,
    val worldPositionX: Float,
    val worldPositionY: Float,
    val worldPositionZ: Float,
    val worldVelocityX: Float,
    val worldVelocityY: Float,
    val worldVelocityZ: Float,
    val worldForwardDirX: Float,
    val worldForwardDirY: Float,
    val worldForwardDirZ: Float,
    val worldRightDirX: Float,
    val worldRightDirY: Float,
    val worldRightDirZ: Float,
    val gForceLateral: Float,
    val gForceLongitudinal: Float,
    val gForceVertical: Float,
    val yaw: Float,
    val pitch: Float,
    val roll: Float
)