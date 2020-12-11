package com.levess007.f1telemetry.data.elements

enum class PitStatus {
    NONE, PITTING, IN_PIT;

    companion object {
        fun fromInt(i: Int): PitStatus {
            return when (i) {
                1 -> PITTING
                2 -> IN_PIT
                else -> NONE
            }
        }
    }
}