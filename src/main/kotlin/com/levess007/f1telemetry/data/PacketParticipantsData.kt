package com.levess007.f1telemetry.data

import com.levess007.f1telemetry.data.elements.ParticipantData

class PacketParticipantsData : Packet() {
    var numCars = 0
    var participants: List<ParticipantData>? = null
}