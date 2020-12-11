package com.levess007.f1telemetry.data

import com.levess007.f1telemetry.data.elements.CarMotionData
import com.levess007.f1telemetry.data.elements.WheelData

class PacketMotionData : Packet() {
    var carMotionDataList: List<CarMotionData>? = null
    var suspensionPosition: WheelData<Float?>? = null
    var suspensionVelocity: WheelData<Float?>? = null
    var suspensionAcceleration: WheelData<Float?>? = null
    var wheelSpeed: WheelData<Float?>? = null
    var wheelSlip: WheelData<Float?>? = null
    var localVelocityX = 0f
    var localVelocityY = 0f
    var localVelocityZ = 0f
    var angularVelocityX = 0f
    var angularVelocityY = 0f
    var angularVelocityZ = 0f
    var angularAccelerationX = 0f
    var angularAccelerationY = 0f
    var angularAccelerationZ = 0f
    var frontWheelsAngle = 0f
}