package com.levess007.f1telemetry.data

import com.levess007.f1telemetry.data.elements.CarStatusData
import com.levess007.f1telemetry.data.elements.Header

class PacketCarStatusData(
    header: Header,
    val carStatuses: List<CarStatusData>?
) : Packet(header)