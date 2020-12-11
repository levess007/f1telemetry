package com.levess007.f1telemetry.data

import com.levess007.f1telemetry.data.elements.ButtonStatus
import com.levess007.f1telemetry.data.elements.CarTelemetryData

class PacketCarTelemetryData : Packet() {
    var carTelemetryData: List<CarTelemetryData>? = null
    var buttonStatus // TODO, create a representation of this data properly
            : ButtonStatus? = null
}