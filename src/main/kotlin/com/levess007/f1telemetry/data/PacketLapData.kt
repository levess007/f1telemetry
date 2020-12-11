package com.levess007.f1telemetry.data

import com.levess007.f1telemetry.data.elements.LapData

class PacketLapData : Packet() {
    var lapDataList: List<LapData>? = null
}