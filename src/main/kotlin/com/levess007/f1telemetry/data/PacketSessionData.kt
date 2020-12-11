package com.levess007.f1telemetry.data

import com.levess007.f1telemetry.data.elements.*

class PacketSessionData : Packet() {
    var weather: Weather? = null
    var trackTemperature = 0
    var airTemperature = 0
    var totalLaps = 0
    var trackLength = 0
    var sessionType: SessionType? = null
    var trackId = 0
    var era: Era? = null
    var sessionTimeLeft = 0
    var sessionDuration = 0
    var pitSpeedLimit = 0
    var isGamePaused = false
    var isSpectating = false
    var spectatorCarIndex = 0
    var isSliProNativeSupport = false
    var numMarshalZones = 0
    var marshalZones: List<MarshalZone>? = null
    var safetyCarStatus: SafetyCarStatus? = null
    var isNetworkGame = false
}