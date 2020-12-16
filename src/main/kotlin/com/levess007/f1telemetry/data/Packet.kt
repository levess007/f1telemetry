package com.levess007.f1telemetry.data

import com.levess007.f1telemetry.data.elements.Header

abstract class Packet(val header: Header) {

    override fun toString(): String {
        return header.toString()
    }

    fun getType(): Int {
        return header.packetId
    }

    open fun getSpeed(): Number {
        return -1
    }
}