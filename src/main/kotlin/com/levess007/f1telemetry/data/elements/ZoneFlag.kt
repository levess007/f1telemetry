package com.levess007.f1telemetry.data.elements

enum class ZoneFlag {
    NONE, GREEN, BLUE, YELLOW, RED, UNKNOWN;

    companion object {
        fun fromInt(i: Int): ZoneFlag {
            return when (i) {
                0 -> NONE
                1 -> GREEN
                2 -> BLUE
                3 -> YELLOW
                4 -> RED
                else -> UNKNOWN
            }
        }
    }
}