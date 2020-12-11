package com.levess007.f1telemetry.data.elements

import java.math.BigInteger

class Header {
    var packetFormat = 0
    var packetMajVersion = 0
    var packetMinVersion = 0
    var packetId = 0
    var sessionUID: BigInteger? = null
    var sessionTime = 0f
    var frameIdentifier: Long = 0
    var playerCarIndex = 0
    override fun toString(): String {
        return "Header :: packetFormat:" + packetFormat +
                ", majversion:" + packetMinVersion +
                ", minversion:" + packetMajVersion +
                ", packetId:" + packetId +
                ", sessionUID:" + sessionUID +
                ", sessionTime:" + sessionTime +
                ", frameIdentifier:" + frameIdentifier +
                ", playerCarIndex:" + playerCarIndex
    }
}