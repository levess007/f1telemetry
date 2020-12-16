package com.levess007.f1telemetry.data

import com.levess007.f1telemetry.data.elements.CarSetupData
import com.levess007.f1telemetry.data.elements.Header

class PacketCarSetupData(
    header: Header,
    val carSetups: List<CarSetupData>?
) : Packet(header)