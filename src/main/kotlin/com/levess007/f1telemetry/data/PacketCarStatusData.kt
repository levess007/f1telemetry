package com.levess007.f1telemetry.data

import com.levess007.f1telemetry.data.elements.CarStatusData

class PacketCarStatusData : Packet() {
    var carStatuses: List<CarStatusData>? = null
}