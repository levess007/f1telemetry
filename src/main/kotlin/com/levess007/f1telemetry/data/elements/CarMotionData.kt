package com.levess007.f1telemetry.data.elements

class CarMotionData {
    var carIndex = 0
    var isPlayersCar = false
    var worldPositionX = 0f
    var worldPositionY = 0f
    var worldPositionZ = 0f
    var worldVelocityX = 0f
    var worldVelocityY = 0f
    var worldVelocityZ = 0f
    var worldForwardDirX = 0f
    var worldForwardDirY = 0f
    var worldForwardDirZ = 0f
    var worldRightDirX = 0f
    var worldRightDirY = 0f
    var worldRightDirZ = 0f
    private var gForceLateral = 0f
    private var gForceLongitudinal = 0f
    private var gForceVertical = 0f
    var yaw = 0f
    var pitch = 0f
    var roll = 0f
    fun getgForceLateral(): Float {
        return gForceLateral
    }

    fun setgForceLateral(gForceLateral: Float) {
        this.gForceLateral = gForceLateral
    }

    fun getgForceLongitudinal(): Float {
        return gForceLongitudinal
    }

    fun setgForceLongitudinal(gForceLongitudinal: Float) {
        this.gForceLongitudinal = gForceLongitudinal
    }

    fun getgForceVertical(): Float {
        return gForceVertical
    }

    fun setgForceVertical(gForceVertical: Float) {
        this.gForceVertical = gForceVertical
    }
}