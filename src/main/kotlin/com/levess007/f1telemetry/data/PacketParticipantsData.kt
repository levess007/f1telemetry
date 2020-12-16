package com.levess007.f1telemetry.data

import com.levess007.f1telemetry.data.elements.Header
import com.levess007.f1telemetry.data.elements.ParticipantData

class PacketParticipantsData(
    header: Header,
    val numCars: Int,
    val participants: List<ParticipantData>?
) : Packet(header)