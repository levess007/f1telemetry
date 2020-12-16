package com.levess007.f1telemetry.data

import com.levess007.f1telemetry.data.elements.Header
import com.levess007.f1telemetry.data.elements.LapData

class PacketLapData(
    header: Header,
    val lapDataList: List<LapData>?
) : Packet(header)