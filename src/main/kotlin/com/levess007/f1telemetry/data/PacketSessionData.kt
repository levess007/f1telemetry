package com.levess007.f1telemetry.data

import com.levess007.f1telemetry.data.elements.*

class PacketSessionData(
    header: Header,
    val weather: Weather?,
    val trackTemperature: Int,
    val airTemperature: Int,
    val totalLaps: Int,
    val trackLength: Int,
    val sessionType: SessionType?,
    val trackId: Int,
    val era: Era?,
    val sessionTimeLeft: Int,
    val sessionDuration: Int,
    val pitSpeedLimit: Int,
    val isGamePaused: Boolean,
    val isSpectating: Boolean,
    val spectatorCarIndex: Int,
    val isSliProNativeSupport: Boolean,
    val numMarshalZones: Int,
    val marshalZones: List<MarshalZone>,
    val safetyCarStatus: SafetyCarStatus?,
    val isNetworkGame: Boolean
) : Packet(header) {

    override fun toString(): String {
        return weather.toString()
    }

    fun fv(){
        print(sessionTimeLeft)
    }
}