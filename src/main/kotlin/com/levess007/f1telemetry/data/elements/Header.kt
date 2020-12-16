package com.levess007.f1telemetry.data.elements

import java.math.BigInteger

data class Header(
    val packetFormat: Int,
    val gameMajVersion: Int,
    val gameMinVersion: Int,
    val packetVersion: Int,
    val packetId: Int,
    val sessionUID: BigInteger?,
    val sessionTime: Float,
    val frameIdentifier: Long,
    val playerCarIndex: Int
) {

    override fun toString(): String {
        return "Header :: packetFormat:" + packetFormat +
                ", majversion:" + gameMinVersion +
                ", minversion:" + gameMajVersion +
                ", packetVersion:" + packetVersion +
                ", packetId:" + packetId +
                ", sessionUID:" + sessionUID +
                ", sessionTime:" + sessionTime +
                ", frameIdentifier:" + frameIdentifier +
                ", playerCarIndex:" + playerCarIndex
    }
}