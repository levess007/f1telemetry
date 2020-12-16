package com.levess007.f1telemetry.data

import com.levess007.f1telemetry.data.elements.CarMotionData
import com.levess007.f1telemetry.data.elements.Header
import com.levess007.f1telemetry.data.elements.WheelData

class PacketMotionData(
    header: Header,
    val carMotionDataList: List<CarMotionData>?,
    val suspensionPosition: WheelData<Float?>?,
    val suspensionVelocity: WheelData<Float?>?,
    val suspensionAcceleration: WheelData<Float?>?,
    val wheelSpeed: WheelData<Float?>?,
    val wheelSlip: WheelData<Float?>?,
    val localVelocityX: Float,
    val localVelocityY: Float,
    val localVelocityZ: Float,
    val angularVelocityX: Float,
    val angularVelocityY: Float,
    val angularVelocityZ: Float,
    val angularAccelerationX: Float,
    val angularAccelerationY: Float,
    val angularAccelerationZ: Float,
    val frontWheelsAngle: Float
) : Packet(header)