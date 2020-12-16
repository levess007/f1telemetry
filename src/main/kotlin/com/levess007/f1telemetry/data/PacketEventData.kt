package com.levess007.f1telemetry.data

import com.levess007.f1telemetry.data.elements.Header

class PacketEventData(header: Header) : Packet(header) {
    var eventCode: String? = null
}