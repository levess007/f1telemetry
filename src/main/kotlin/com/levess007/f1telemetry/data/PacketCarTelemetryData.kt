package com.levess007.f1telemetry.data

import com.levess007.f1telemetry.data.elements.ButtonStatus
import com.levess007.f1telemetry.data.elements.CarTelemetryData
import com.levess007.f1telemetry.data.elements.Header

class PacketCarTelemetryData(
    header: Header,
    val carTelemetryData: List<CarTelemetryData>?,// TODO, create a representation of this data properly
    val buttonStatus: ButtonStatus?
) : Packet(header) {

    override fun getSpeed(): Number {
        return carTelemetryData!![header.playerCarIndex].speed
    }
}