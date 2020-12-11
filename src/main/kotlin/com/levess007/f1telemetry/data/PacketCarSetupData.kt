package com.levess007.f1telemetry.data

import com.levess007.f1telemetry.data.elements.CarSetupData

class PacketCarSetupData : Packet() {
    var carSetups: List<CarSetupData>? = null
}